package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.RecoveryCompleted;
import akka.persistence.SnapshotOffer;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventEnvelope;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.entities.states.StateEnvelope;
import com.cloudcommander.vendor.ddd.entities.states.StateFactory;


import java.util.List;


public class EntityActor<U, S extends com.cloudcommander.vendor.ddd.entities.states.State, F extends com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState> extends AbstractPersistentActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private StateEnvelope<F, S> stateEnvelope;

    private final String persistenceId;

    private EntityDefinition<U, S, F> entityDefinition;

    public EntityActor(EntityDefinition<U, S, F> entityDefinition){
        this.entityDefinition = entityDefinition;

        persistenceId = createPersistenceId(entityDefinition);

        this.stateEnvelope = createInitialStateEnvelope();
    }

    private StateEnvelope<F, S> createInitialStateEnvelope() {
        final StateFactory<S> stateFactory = entityDefinition.getStateFactory();

        final F initialFsmState = entityDefinition.getInitialFSMState();
        final S state = stateFactory.create();

         return new StateEnvelope<>(initialFsmState, state);
    }

    protected void addEventHandlers(ReceiveBuilder receiveBuilder) {
        //Inner event
        final Receive eventsReceive = createEventsReceive(entityDefinition.getEventHandlers());

        //Event envelope
        receiveBuilder.match(EventEnvelope.class, eventEnvelope -> {
            F newFsmState = (F)eventEnvelope.getNewFSMState();
            Event<U> event = (Event<U>)eventEnvelope.getEvent();

            eventsReceive.onMessage().apply(event);

            if(newFsmState != null){
                stateEnvelope = stateEnvelope.withFsmState(newFsmState);
                setFsmState(newFsmState);
            }
        });
    }

    protected Receive createEventsReceive(List<? extends EventHandler<U, ? extends Event<U>, S>> eventHandlers){
        final ReceiveBuilder eventsReceiveBuilder = ReceiveBuilder.create();
        for(EventHandler<U, ? extends Event<U>, S> eventHandler: eventHandlers){
           addEventHandler(eventsReceiveBuilder, eventHandler);
        }

        //Fallback
        eventsReceiveBuilder.matchAny(message -> {
            log.warning("Unhandled event {} in actor {}", message, getSelf().path());
        });

        return eventsReceiveBuilder.build();
    }

    protected <E extends Event<U>> void addEventHandler(ReceiveBuilder eventsReceiveBuilder, EventHandler<U, E, S> eventHandler) {
        Class<E> eventClass = eventHandler.getEventClass();
        eventsReceiveBuilder.match(eventClass, event -> {
            final S state = stateEnvelope.getState();
            final S newState = eventHandler.handle(event, state);

            stateEnvelope = stateEnvelope.withState(newState);
        });
    }

    private String createPersistenceId(EntityDefinition<U, S, F> entityDefinition){
        String aggregateName = entityDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = entityDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + aggregateName + '-' + getSelf().path().name();
    }


    @Override
    public String persistenceId() {
        return persistenceId;
    }

    public static <U, S extends com.cloudcommander.vendor.ddd.entities.states.State, F extends com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState> Props props(final EntityDefinition<U, S, F> entityDefinition) {
        return Props.create(EntityActor.class, () -> new EntityActor<>(entityDefinition));
    }

    @Override
    public Receive createReceiveRecover() {
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Restore from snapshots
        receiveBuilder.match(SnapshotOffer.class, snapshotOffer -> {
            stateEnvelope = (StateEnvelope<F,S>)snapshotOffer.snapshot();

            switchToFsmState(stateEnvelope.getFsmState());
        });

        //Handle defined events
        addEventHandlers(receiveBuilder);

        return receiveBuilder.build();
    }

    @Override
    public Receive createReceive() {
        return createReceive(entityDefinition.getInitialFSMState());
    }

    protected void setFsmState(F fsmState){
        Receive receive = createReceive(fsmState);
        getContext().become(receive);
    }

    protected Receive createReceive(F fsmState){
        final ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        addStateQueryHandlers(fsmState, receiveBuilder, entityDefinition.getStateQueryHandlers());

        addStateCommandHandlers(fsmState, receiveBuilder, entityDefinition.getStateCommandHandlers());

        //Unhandled message
        receiveBuilder.matchAny(message -> {
            final UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(message.getClass());
            getSender().tell(unhandledCommandResponse, getSelf());
        });

        return receiveBuilder.build();
    }

    protected void addStateQueryHandlers(F fsmState, final ReceiveBuilder receiveBuilder, List<? extends StateQueryHandlers<U, S, F>> stateQueryHandlers){
        for(final StateQueryHandlers<U, S, F> stateQueryHandler: stateQueryHandlers){
            final F queryHandlersState = stateQueryHandler.getFsmState();
            if(fsmState.equals(queryHandlersState)){
                final List<? extends QueryHandler<U, S>> queryHandlers = stateQueryHandler.getQueryHandlers();
                for (final QueryHandler<U, S> queryHandler : queryHandlers) {
                    final Class<? extends Query<U>> queryClass = queryHandler.getQueryClass();
                    receiveBuilder.match(queryClass, query -> {
                        final S state = stateEnvelope.getState();
                        final Result<U> queryResult = queryHandler.handle(query, state);

                        getSender().tell(queryResult, getSelf());
                    });
                }
            }
        }
    }

    protected void addStateCommandHandlers(F fsmState, ReceiveBuilder receiveBuilder, List<? extends StateCommandHandlers<U, S, F>> stateCommandHandlersList) {
        //Handle commands
        for (StateCommandHandlers<U, S, F> stateCommandHandlers : stateCommandHandlersList) {
            //TODO tello optimize
            final F commandHandlersState = stateCommandHandlers.getFsmState();
            if(fsmState.equals(commandHandlersState)){
                addCommandHandlers(receiveBuilder, stateCommandHandlers.getCommandHandlers());
            }
        }
    }

    protected <C extends Command<U>> void addCommandHandlers(ReceiveBuilder receiveBuilder, List<? extends CommandHandler<U, S, F>> commandHandlers) {
        //Events receive
        final ReceiveBuilder eventHandlersReceiveBuilder = ReceiveBuilder.create();
        addEventHandlers(eventHandlersReceiveBuilder);

        eventHandlersReceiveBuilder.matchAny(message -> {
            log.warning("Unhandled event {} in actor {}", message, getSelf().path());
        });

        final Receive eventHandlersReceive = eventHandlersReceiveBuilder.build();

        //Command handlers
        for (CommandHandler<U, S, F> commandHandler : commandHandlers) {
            final Class<? extends Command<U>> commandClass = commandHandler.getCommandClass();
            receiveBuilder.match(commandClass, command -> {
                final S state = stateEnvelope.getState();
                final CommandHandler.CommandHandlerResult<? extends Event<U>, F> commandHandlerResult = commandHandler.handle(command, state);

                final Event<U> event = commandHandlerResult.getEvent();
                final F newFsmState = commandHandlerResult.getNewFsmState();

                final U aggregateId = command.getAggregateId();
                final EventEnvelope<U, F> eventEnvelope = new EventEnvelope<>(aggregateId, event, newFsmState);
                persist(eventEnvelope, persistedEvent -> {
                    eventHandlersReceive.onMessage().apply(persistedEvent);

                    if(newFsmState != null){
                        switchToFsmState(stateEnvelope.getFsmState());
                    }

                    getSender().tell(eventEnvelope, getSelf());
                });
            });
        }
    }

    protected void switchToFsmState(F fsmState){
        final Receive stateReceive = createReceive(fsmState);
        getContext().become(stateReceive);
    }
}
