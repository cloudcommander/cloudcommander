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


import java.util.List;


public class EntityActor<U, BC extends Command<U>, BE extends com.cloudcommander.vendor.ddd.entities.events.Event<U>, BQ extends Query<U>, BR extends Result<U>, S extends com.cloudcommander.vendor.ddd.entities.states.State, F extends com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState> extends AbstractPersistentActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private StateEnvelope<F, S> stateEnvelope;

    private final String persistenceId;

    private EntityDefinition<U, BC, BE, BQ, BR, S, F> entityDefinition;

    public EntityActor(EntityDefinition<U, BC, BE, BQ, BR, S, F> entityDefinition){
        this.entityDefinition = entityDefinition;

        persistenceId = createPersistenceId(entityDefinition);
    }

    protected void addEventHandlers(ReceiveBuilder receiveBuilder) {
        //Inner event
        final Receive eventsReceive = createEventsReceive(entityDefinition.getEventHandlers());

        //Event envelope
        receiveBuilder.match(EventEnvelope.class, eventEnvelope -> {
            F newFsmState = (F)eventEnvelope.getNewFSMState();
            BE event = (BE)eventEnvelope.getEvent();

            eventsReceive.onMessage().apply(event);

            if(newFsmState != null){
                stateEnvelope = stateEnvelope.withFsmState(newFsmState);
                setFsmState(newFsmState);
            }
        });
    }

    protected <ES extends BE> Receive createEventsReceive(List<? extends EventHandler<U, ? extends BE, S, ES>> eventHandlers){
        final ReceiveBuilder eventsReceiveBuilder = ReceiveBuilder.create();
        for(EventHandler<U, ? extends BE, S, ES> eventHandler: eventHandlers){
            Class<ES> eventClass = eventHandler.getEventClass();
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

        return eventsReceiveBuilder.build();
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

        addStateQueryHandlers(fsmState, receiveBuilder, entityDefinition.getStateQueryHandlers());

        addStateCommandHandlers(fsmState, receiveBuilder, entityDefinition.getStateCommandHandlers());

        //Unhandled message
        receiveBuilder.matchAny(message -> {
            final UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(message.getClass());
            getSender().tell(unhandledCommandResponse, getSelf());
        });

        return receiveBuilder.build();
    }

    protected void addStateQueryHandlers(F fsmState, final ReceiveBuilder receiveBuilder, List<? extends StateQueryHandlers<U, BQ, BR, S, F>> stateQueryHandlers){
        for(StateQueryHandlers<U, BQ, BR, S, F> stateQueryHandler: stateQueryHandlers){
            final F queryHandlersState = stateQueryHandler.getFsmState();
            if(fsmState.equals(queryHandlersState)){
                addQueryHandlers(receiveBuilder, stateQueryHandler.getQueryHandlers());
            }
        }
    }

    protected <QS extends BQ> void addQueryHandlers(final ReceiveBuilder receiveBuilder, final List<? extends QueryHandler<U, ? extends BQ, ? extends BR, S, QS>> queryHandlers){
        for (QueryHandler<U, ? extends BQ, ? extends BR, S, QS> queryHandler : queryHandlers) {
            final Class<QS> queryClass = queryHandler.getQueryClass();
            receiveBuilder.match(queryClass, query -> {
                final S state = stateEnvelope.getState();
                final Result<U> queryResult = queryHandler.handle(query, state);

                getSelf().tell(queryResult, getSender());
            });
        }
    }

    protected void addStateCommandHandlers(F fsmState, ReceiveBuilder receiveBuilder, List<? extends StateCommandHandlers<U, BC, BE, S, F>> stateCommandHandlersList) {
        //Handle commands
        for (StateCommandHandlers<U, BC, BE, S, F> stateCommandHandlers : stateCommandHandlersList) {
            //TODO tello optimize
            final F commandHandlersState = stateCommandHandlers.getFsmState();
            if(fsmState.equals(commandHandlersState)){
                addCommandHandlers(receiveBuilder, stateCommandHandlers.getCommandHandlers());
            }
        }
    }

    protected <C extends BC> void addCommandHandlers(ReceiveBuilder receiveBuilder, List<? extends CommandHandler<U, BC, BE, S, F, C>> commandHandlers) {
        //Events receive
        final ReceiveBuilder eventHandlersReceiveBuilder = ReceiveBuilder.create();
        addEventHandlers(eventHandlersReceiveBuilder);

        eventHandlersReceiveBuilder.matchAny(message -> {
            log.warning("Unhandled event {} in actor {}", message, getSelf().path());
        });

        final Receive eventHandlersReceive = eventHandlersReceiveBuilder.build();

        //Command handler
        for (CommandHandler<U, ? extends BC, ? extends BE, S, F, C> commandHandler : commandHandlers) {
            final Class<C> commandClass = commandHandler.getCommandClass();
            receiveBuilder.match(commandClass, command -> {
                final S state = stateEnvelope.getState();
                final CommandHandler.CommandHandlerResult<? extends BE, F> commandHandlerResult = commandHandler.handle(command, state);

                final BE event = commandHandlerResult.getEvent();
                final F newFsmState = commandHandlerResult.getNewFsmState();

                final EventEnvelope<U, BE, F> eventEnvelope = new EventEnvelope<>(event, newFsmState);
                persist(eventEnvelope, persistedEvent -> {
                    eventHandlersReceive.onMessage().apply(persistedEvent);
                });
            });
        }
    }
}
