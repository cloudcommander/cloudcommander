package com.cloudcommander.vendor.ddd.aggregates.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.actor.PoisonPill;
import akka.actor.Terminated;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.fsmstates.CounterFSMState;
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
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.handlers.IncrementCommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.handers.ValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.queries.GetValueQuery;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.queries.handlers.GetValueQueryHandler;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.results.ValueResult;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.state.CounterStateFactory;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class AggregateActorUnitTest{

    private StateFactory stateFactory = new CounterStateFactory();
    private BoundedContextDefinition counterBoundedContextDefinition = new BoundedContextDefinition("Counter");

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("AggregateActorUnitTest");
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testNonMappedCommand(){
        new TestKit(system) {{
            StateCommandHandlers countingStateCommandHandlers = new StateCommandHandlers(CounterFSMState.COUNTING, Collections.emptyList());
            List<StateCommandHandlers> stateCommandHandlers = Collections.singletonList(countingStateCommandHandlers);

            Map<FSMState, EventHandler> eventHandlerMap = Collections.emptyMap();

            StateQueryHandlers countingStateQueryHandlers = new StateQueryHandlers(CounterFSMState.COUNTING, Collections.emptyList());
            List<StateQueryHandlers> stateQueryHandlers = Collections.singletonList(countingStateQueryHandlers);

            AggregateDefinition aggregateDefinition = new AggregateDefinition("Counter", counterBoundedContextDefinition, stateFactory, stateCommandHandlers, eventHandlerMap, stateQueryHandlers, CounterFSMState.COUNTING);

            final ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition));
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
            List<CommandHandler<? extends Command, ? extends Event, ? extends State>> countingCommandHandlers = Collections.singletonList(new IncrementCommandHandler());
            StateCommandHandlers countingStateCommandHandlers = new StateCommandHandlers(CounterFSMState.COUNTING, countingCommandHandlers);
            List<StateCommandHandlers> stateCommandHandlers = Collections.singletonList(countingStateCommandHandlers);

            Map<Class<? extends Event>, EventHandler> eventHandlerMap = new HashMap<>();
            eventHandlerMap.put(ValueChangedEvent.class, new ValueChangedEventHandler());

            List<QueryHandler<? extends Query, ? extends Result, ? extends State>> countingQueryHandlers = Collections.singletonList(new GetValueQueryHandler());
            StateQueryHandlers countingStateQueryHandlers = new StateQueryHandlers(CounterFSMState.COUNTING, countingQueryHandlers);
            List<StateQueryHandlers> stateQueryHandlers = Collections.singletonList(countingStateQueryHandlers);

            AggregateDefinition aggregateDefinition = new AggregateDefinition("Counter", counterBoundedContextDefinition, stateFactory, stateCommandHandlers, eventHandlerMap, stateQueryHandlers, CounterFSMState.COUNTING);

            ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition), "testCounter");
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

                aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition), "testCounter");

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