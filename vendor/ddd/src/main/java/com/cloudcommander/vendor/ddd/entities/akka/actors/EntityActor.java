package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.EventEnvelope;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.entities.states.StateEnvelope;


import java.util.Map;


public class EntityActor<U, C extends Command<U>, E extends com.cloudcommander.vendor.ddd.entities.events.Event<U>, Q extends Query<U>, R extends Result<U>, S extends com.cloudcommander.vendor.ddd.entities.states.State, F extends com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState> extends AbstractPersistentActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private StateEnvelope<F, S> stateEnvelope;

    private final String persistenceId;

    private EntityDefinition<U, C, E, Q, R, S, F> entityDefinition;

    public EntityActor(EntityDefinition<U, C, E, Q, R, S, F> entityDefinition){
        this.entityDefinition = entityDefinition;

        persistenceId = createPersistenceId(entityDefinition);
    }

    protected void addEventHandlers(ReceiveBuilder receiveBuilder) {
        //Inner event
        final ReceiveBuilder eventsReceiveBuilder = ReceiveBuilder.create();
        final Map<E, ? extends EventHandler<U, E, S>> eventHandlerMap = entityDefinition.getEventHandlerMap();
        for(EventHandler<U, E, S> eventHandler: eventHandlerMap.values()){
            Class<E> eventClass = eventHandler.getEventClass();
            eventsReceiveBuilder.match(eventClass, event -> {
                final S state = stateEnvelope.getState();
                final S newState = eventHandler.handle(event, state);

                stateEnvelope = stateEnvelope.withState(newState);
            });
        }

        //Fallback
        eventsReceiveBuilder.matchAny(message -> {
            log.warning("Unhandled event {} in actor {}", message, getSelf().path());
        });

        final Receive eventsReceive = eventsReceiveBuilder.build();

        //Event envelope
        receiveBuilder.match(EventEnvelope.class, eventEnvelope -> {
            F newFsmState = (F)eventEnvelope.getNewFSMState();
            E event = (E)eventEnvelope.getEvent();

            eventsReceive.onMessage().apply(event);

            if(newFsmState != null){
                stateEnvelope = stateEnvelope.withFsmState(newFsmState);
                setFsmState(newFsmState);
            }
        });
    }

    private String createPersistenceId(EntityDefinition entityDefinition){
        String aggregateName = entityDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = entityDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + aggregateName + '-' + getSelf().path().name();
    }


    @Override
    public String persistenceId() {
        return persistenceId;
    }

    public static <U, C extends Command<U>, E extends com.cloudcommander.vendor.ddd.entities.events.Event<U>, Q extends Query<U>, R extends Result<U>, S extends com.cloudcommander.vendor.ddd.entities.states.State, F extends com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState> Props props(final EntityDefinition<U, C, E, Q, R, S, F> entityDefinition) {
        return Props.create(EntityActor.class, () -> new EntityActor<>(entityDefinition));
    }

    @Override
    public Receive createReceiveRecover() {
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Restore from snapshots
        receiveBuilder.match(SnapshotOffer.class, snapshotOffer -> {
            stateEnvelope = (StateEnvelope<F,S>)snapshotOffer.snapshot();
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

        //Handle queries
        for (StateQueryHandlers<U, Q, R, S, F> stateQueryHandlers : entityDefinition.getStateQueryHandlersList()) {
            //TODO tello optimize
            final F queryHandlersState = stateQueryHandlers.getFsmState();
            if(fsmState.equals(queryHandlersState)){
                for (QueryHandler<U, Q, R, S> queryHandler : stateQueryHandlers.getQueryHandlers()) {
                    final Class<Q> queryClass = queryHandler.getQueryClass();
                    receiveBuilder.match(queryClass, query -> {
                        final S state = stateEnvelope.getState();
                        final Result<U> queryResult = queryHandler.handle(query, state);

                        getSelf().tell(queryResult, getSender());
                    });
                }
            }
        }

        //Events receive
        final ReceiveBuilder eventHandlersReceiveBuilder = ReceiveBuilder.create();
        addEventHandlers(eventHandlersReceiveBuilder);

        eventHandlersReceiveBuilder.matchAny(message -> {
            log.warning("Unhandled event {} in actor {}", message, getSelf().path());
        });

        final Receive eventHandlersReceive = eventHandlersReceiveBuilder.build();

        //Handle commands
        for (StateCommandHandlers<U, C, E, S, F> stateCommandHandlers : entityDefinition.getStateCommandHandlersList()) {
            //TODO tello optimize
            final F commandHandlersState = stateCommandHandlers.getFsmState();
            if(fsmState.equals(commandHandlersState)){
                for (CommandHandler<U, C, E, S, F> commandHandler : stateCommandHandlers.getCommandHandlers()) {
                    final Class<C> commandClass = commandHandler.getCommandClass();
                    receiveBuilder.match(commandClass, command -> {
                        final S state = stateEnvelope.getState();
                        final CommandHandler.CommandHandlerResult<E, F> commandHandlerResult = commandHandler.handle(command, state);

                        final E event = commandHandlerResult.getEvent();
                        final F newFsmState = commandHandlerResult.getNewFsmState();

                        final EventEnvelope<U, E, F> eventEnvelope = new EventEnvelope<>(event, newFsmState);
                        persist(eventEnvelope, persistedEvent -> {
                            eventHandlersReceive.onMessage().apply(persistedEvent);
                        });
                    });
                }
            }
        }

        //Unhandled message
        receiveBuilder.matchAny(message -> {
            final UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(message.getClass());
            getSender().tell(unhandledCommandResponse, getSelf());
        });

        return receiveBuilder.build();
    }
}
