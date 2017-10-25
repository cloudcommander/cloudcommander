package com.cloudcommander.vendor.ddd.aggregates.akka.actors;

import akka.actor.Props;
import akka.japi.pf.FI;
import akka.persistence.fsm.AbstractPersistentFSM;

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
import scala.collection.Seq;
import scala.collection.immutable.Set;

import java.util.List;
import java.util.Map;


public class AggregateActor extends AbstractPersistentFSM<FSMState, State, Event> {

    private final Map<? extends com.cloudcommander.vendor.ddd.aggregates.events.Event, ? extends EventHandler<Object, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State>> eventHandlerMap;

    private final String persistenceId;

    public AggregateActor(AggregateDefinition<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> aggregateDefinition){
        eventHandlerMap = aggregateDefinition.getEventHandlerMap();

        persistenceId = createPersistenceId(aggregateDefinition);

        setUpFSM(aggregateDefinition);

    }

    private void setUpFSM(AggregateDefinition<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> aggregateDefinition) {
        StateFactory<com.cloudcommander.vendor.ddd.aggregates.states.State> stateFactory = aggregateDefinition.getStateFactory();
        com.cloudcommander.vendor.ddd.aggregates.states.State initialState = stateFactory.create();

        startWith(aggregateDefinition.getInitialFSMState(), initialState);

        //Setup query handlers
        {
            final List<? extends StateQueryHandlers<Object, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState>> stateQueryHandlersList = aggregateDefinition.getStateQueryHandlersList();
            for(StateQueryHandlers<Object, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> stateQueryHandlers: stateQueryHandlersList){
                final com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState fsmState = stateQueryHandlers.getFsmState();
                final List<? extends QueryHandler<Object, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State>> queryHandlers = stateQueryHandlers.getQueryHandlers();

                FSMStateFunctionBuilder<com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event> builder = null;

                for(QueryHandler<Object, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State> queryHandler: queryHandlers){
                    final Class<Query<Object>> queryClass = queryHandler.getQueryClass();
                    FI.Apply2<Query<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, State<com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event>> apply2 = (query, state) -> {
                        Result<Object> result = queryHandler.handle(query, state);

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
            final List<? extends StateCommandHandlers<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState>> stateCommandHandlersList = aggregateDefinition.getStateCommandHandlersList();
            for(StateCommandHandlers<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> stateCommandHandlers: stateCommandHandlersList){
                com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState fsmState = stateCommandHandlers.getFsmState();
                List<? extends CommandHandler<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState>> commandHandlers = stateCommandHandlers.getCommandHandlers();

                FSMStateFunctionBuilder<com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event> builder = null;

                for(CommandHandler<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> commandHandler: commandHandlers){
                    Class<Command<Object>> commandClass = commandHandler.getCommandClass();
                    FI.Apply2<Command<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, State<com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event>> apply2 = (command, state) -> {
                        CommandHandler.CommandHandlerResult<com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> commandHandlerResult = commandHandler.handle(command, state);

                        com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState newFsmState = commandHandlerResult.getNewFsmState();
                        com.cloudcommander.vendor.ddd.aggregates.events.Event event = commandHandlerResult.getEvent();

                        State<com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.events.Event> stateFn;
                        if(newFsmState != null){
                            stateFn = goTo(newFsmState);
                        }else {
                            stateFn = stay();
                        }

                        final Seq<com.cloudcommander.vendor.ddd.aggregates.events.Event> eventsSeq =  new Set.Set1<>(event).toSeq();
                        return stateFn.applying(eventsSeq).replying(event);
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
    public String persistenceId() {
        return persistenceId;
    }

    public static Props props(final AggregateDefinition<Object, Command<Object>, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, Query<Object>, Result<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State, com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState> aggregateDefinition) {
        return Props.create(AggregateActor.class, () -> new AggregateActor(aggregateDefinition));
    }

    @Override
    public Class<com.cloudcommander.vendor.ddd.aggregates.events.Event> domainEventClass() {
        return com.cloudcommander.vendor.ddd.aggregates.events.Event.class;
    }

    @Override
    public com.cloudcommander.vendor.ddd.aggregates.states.State applyEvent(com.cloudcommander.vendor.ddd.aggregates.events.Event event, com.cloudcommander.vendor.ddd.aggregates.states.State state) {
        Class<? extends com.cloudcommander.vendor.ddd.aggregates.events.Event> eventClass = event.getClass();
        final EventHandler<Object, com.cloudcommander.vendor.ddd.aggregates.events.Event<Object>, com.cloudcommander.vendor.ddd.aggregates.states.State> eventHandler = eventHandlerMap.get(eventClass);
        if(eventHandler == null){
            throw new RuntimeException("Unhandled event: [" + eventClass + "]");
        }

        return eventHandler.handle(event, state);
    }
}
