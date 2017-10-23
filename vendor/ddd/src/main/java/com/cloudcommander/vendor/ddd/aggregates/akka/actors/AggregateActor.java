package com.cloudcommander.vendor.ddd.aggregates.akka.actors;

import akka.actor.Props;
import akka.japi.pf.FI;
import akka.persistence.fsm.AbstractPersistentFSM;
import akka.persistence.fsm.PersistentFSM;

import akka.persistence.fsm.japi.pf.FSMStateFunctionBuilder;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.queries.Query;
import com.cloudcommander.vendor.ddd.aggregates.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.aggregates.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.List;
import java.util.Map;


public class AggregateActor extends AbstractPersistentFSM<PersistentFSM.FSMState, State, Event> {

    private final Map<Event, EventHandler> eventHandlerMap;

    private final String persistenceId;

    public AggregateActor(AggregateDefinition aggregateDefinition){

        eventHandlerMap = aggregateDefinition.getEventHandlerMap();

        persistenceId = createPersistenceId(aggregateDefinition);

        setUpFSM(aggregateDefinition);
    }

    private void setUpFSM(AggregateDefinition aggregateDefinition) {
        StateFactory stateFactory = aggregateDefinition.getStateFactory();
        com.cloudcommander.vendor.ddd.aggregates.states.State initialState = stateFactory.create();

        startWith(aggregateDefinition.getInitialFSMState(), initialState);

        //Setup query handlers
        {
            final List<StateQueryHandlers> stateQueryHandlersList = aggregateDefinition.getStateQueryHandlersList();
            for(StateQueryHandlers stateQueryHandlers: stateQueryHandlersList){
                FSMState fsmState = stateQueryHandlers.getFsmState();
                List<QueryHandler> queryHandlers = stateQueryHandlers.getQueryHandlers();

                FSMStateFunctionBuilder builder = null;

                for(QueryHandler queryHandler: queryHandlers){
                    Class queryClass = queryHandler.getQueryClass();
                    FI.Apply2<Query, com.cloudcommander.vendor.ddd.aggregates.states.State, State<FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event>> apply2 = (query, state) -> {
                        Result result = queryHandler.handle(query, state);
                        return stay().replying(result);
                    };

                    if(builder == null){
                        builder = matchEvent(queryClass, apply2);
                    }else{
                        builder.event(queryClass, apply2);
                    }
                }

                if(builder != null){
                    when(fsmState, builder);
                }
            }
        }

        //Setup command handlers
        {
            final List<StateCommandHandlers> stateCommandHandlersList = aggregateDefinition.getStateCommandHandlersList();
            for(StateCommandHandlers stateCommandHandlers: stateCommandHandlersList){
                FSMState fsmState = stateCommandHandlers.getFsmState();
                List<CommandHandler> commandHandlers = stateCommandHandlers.getCommandHandlers();

                FSMStateFunctionBuilder builder = null;

                for(CommandHandler commandHandler: commandHandlers){
                    Class commandClass = commandHandler.getCommandClass();
                    FI.Apply2<Command, com.cloudcommander.vendor.ddd.aggregates.states.State, State<FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event>> apply2 = (command, state) -> {
                        CommandHandler.CommandHandlerResult<com.cloudcommander.vendor.ddd.aggregates.events.Event, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> commandHandlerResult = commandHandler.handle(command, state);

                        com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState newFsmState = commandHandlerResult.getNewFsmState();
                        com.cloudcommander.vendor.ddd.aggregates.events.Event event = commandHandlerResult.getEvent();

                        State<FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event> stateFn = null;
                        if(newFsmState != null){
                            stateFn = goTo(newFsmState);
                        }else{
                            stateFn = stay();
                        }
                        return stateFn.applying(event).replying(event);
                    };

                    if(builder == null){
                        builder = matchEvent(commandClass, apply2);
                    }else{
                        builder.event(commandClass, apply2);
                    }
                }

                //Fallback command handler
                if(builder == null){
                    builder = matchAnyEvent((command, state) -> {
                        final UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(command.getClass());

                        return stay().replying(unhandledCommandResponse);
                    });
                }else{
                    builder.anyEvent((command, o2) -> {
                        final UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(command.getClass());

                        return stay().replying(unhandledCommandResponse);
                    });
                }

                when(fsmState, builder);
            }
        }
    }

    private String createPersistenceId(AggregateDefinition aggregateDefinition){
        String aggregateName = aggregateDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = aggregateDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + aggregateName + '-' + getSelf().path().name();
    }

    @Override
    public com.cloudcommander.vendor.ddd.aggregates.states.State applyEvent(com.cloudcommander.vendor.ddd.aggregates.events.Event event, com.cloudcommander.vendor.ddd.aggregates.states.State state) {
        Class<? extends com.cloudcommander.vendor.ddd.aggregates.events.Event> eventClass = event.getClass();
        EventHandler eventHandler = eventHandlerMap.get(eventClass);
        if(eventHandler == null){
            throw new RuntimeException("Unhandled event: [" + eventClass + "]");
        }

        return eventHandler.handle(event, state);
    }

    @Override
    public Class<com.cloudcommander.vendor.ddd.aggregates.events.Event> domainEventClass() {
        return com.cloudcommander.vendor.ddd.aggregates.events.Event.class;
    }

    @Override
    public String persistenceId() {
        return persistenceId;
    }

    public static Props props(final AggregateDefinition aggregateDefinition) {
        return Props.create(AggregateActor.class, () -> new AggregateActor(aggregateDefinition));
    }
}
