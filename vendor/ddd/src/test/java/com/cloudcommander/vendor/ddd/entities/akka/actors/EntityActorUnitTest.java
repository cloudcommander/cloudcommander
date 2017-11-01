package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.actor.PoisonPill;
import akka.actor.Terminated;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.handlers.IncrementCommandHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.handers.ValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.fsmstates.CounterFSMState;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.GetValueQuery;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.handlers.GetValueQueryHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.results.ValueResult;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterState;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;
import com.cloudcommander.vendor.ddd.entities.states.StateFactory;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterStateFactory;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class EntityActorUnitTest {

    private StateFactory<CounterState> stateFactory = new CounterStateFactory();
    private BoundedContextDefinition counterBoundedContextDefinition = new BoundedContextDefinition("Counter");

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("EntityActorUnitTest");
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testNonMappedCommand(){
        new TestKit(system) {{
            StateCommandHandlers.StateCommandHandlersBuilder<UUID, Command<UUID>, Event<UUID> , CounterState, CounterFSMState> stateCommandHandlersBuilder = StateCommandHandlers.builder();
            stateCommandHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            final StateCommandHandlers<UUID, Command<UUID>, Event<UUID> , CounterState, CounterFSMState> countingStateCommandHandlers = stateCommandHandlersBuilder.build();

            StateQueryHandlers.StateQueryHandlersBuilder<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> countingStateQueryHandlersBuilder = StateQueryHandlers.builder();
            countingStateQueryHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            StateQueryHandlers<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> countingStateQueryHandlers = countingStateQueryHandlersBuilder.build();

            final EntityDefinition<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> entityDefinition = EntityDefinition.<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState>builder()
                    .name("Counter")
                    .boundedContextDefinition(counterBoundedContextDefinition)
                    .stateFactory(stateFactory)
                    .stateCommandHandler(countingStateCommandHandlers)
                    .stateQueryHandler(countingStateQueryHandlers)
                    .initialFSMState(CounterFSMState.COUNTING)
                    .build();

            final ActorRef aggregateRef = system.actorOf(EntityActor.props(entityDefinition));
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();
            IncrementCommand incrementCommand = IncrementCommand
                    .builder()
                    .aggregateId(uuid)
                    .build();
            aggregateRef.tell(incrementCommand, probe);
            expectMsgClass(UnhandledCommandResponse.class);
        }};
    }

    @Test
    public void testCounterAggregate(){
        new TestKit(system) {{
            final EntityDefinition.EntityDefinitionBuilder<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> entityDefinitionBuilder = EntityDefinition.builder();

            StateCommandHandlers.StateCommandHandlersBuilder<UUID, Command<UUID>, Event<UUID> , CounterState, CounterFSMState> stateCommandHandlersBuilder = StateCommandHandlers.builder();
            stateCommandHandlersBuilder.commandHandler(new IncrementCommandHandler());
            stateCommandHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            final StateCommandHandlers<UUID, Command<UUID>, Event<UUID> , CounterState, CounterFSMState> countingStateCommandHandlers = stateCommandHandlersBuilder.build();

            StateQueryHandlers.StateQueryHandlersBuilder<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> countingStateQueryHandlersBuilder = StateQueryHandlers.builder();
            countingStateQueryHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            countingStateQueryHandlersBuilder.queryHandler(new GetValueQueryHandler());
            StateQueryHandlers<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> countingStateQueryHandlers = countingStateQueryHandlersBuilder.build();


            final EntityDefinition<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> entityDefinition = entityDefinitionBuilder
                    .name("Counter")
                    .boundedContextDefinition(counterBoundedContextDefinition)
                    .stateFactory(stateFactory)
                    .initialFSMState(CounterFSMState.COUNTING)
                    .stateCommandHandler(countingStateCommandHandlers)
                    .stateQueryHandler(countingStateQueryHandlers)
                    .eventHandler(new ValueChangedEventHandler())
                    .build();
            ActorRef aggregateRef = system.actorOf(EntityActor.props(entityDefinition), "testCounter");
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();

            {
                GetValueQuery getValueQuery = GetValueQuery
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(getValueQuery, probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueResult);

                ValueResult valueResult = (ValueResult) firstMessage;
                Assert.assertEquals(0, valueResult.getValue());
            }
            {
                IncrementCommand incrementCommand = IncrementCommand
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(incrementCommand, probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getAggregateId());
                Assert.assertEquals(1, valueChangedEvent.getNewValue());
            }

            {
                IncrementCommand incrementCommand = IncrementCommand
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(incrementCommand, probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getAggregateId());
                Assert.assertEquals(2, valueChangedEvent.getNewValue());
            }

            {
                watch(aggregateRef);
                aggregateRef.tell(PoisonPill.getInstance(), probe);
                expectMsgClass(Terminated.class);

                aggregateRef = system.actorOf(EntityActor.props(entityDefinition), "testCounter");

                IncrementCommand incrementCommand = IncrementCommand
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(incrementCommand, probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getAggregateId());
                Assert.assertEquals(3, valueChangedEvent.getNewValue());
            }

            {
                GetValueQuery getValueQuery = GetValueQuery
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(getValueQuery, probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueResult);

                ValueResult valueResult = (ValueResult) firstMessage;
                Assert.assertEquals(3, valueResult.getValue());
            }
        }};
    }
}