package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.actor.PoisonPill;
import akka.actor.Terminated;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.Message;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.handlers.IncrementCommandHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.handers.ValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.fsmstates.CounterFSMState;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.GetValueQuery;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.handlers.GetValueQueryHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.results.ValueResult;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterState;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventEnvelope;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.responses.UnhandledCommandResponse;
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

import static akka.testkit.JavaTestKit.duration;

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
            StateCommandHandlers.StateCommandHandlersBuilder<UUID, CounterState, CounterFSMState> stateCommandHandlersBuilder = StateCommandHandlers.builder();
            stateCommandHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            final StateCommandHandlers<UUID, CounterState, CounterFSMState> countingStateCommandHandlers = stateCommandHandlersBuilder.build();

            StateQueryHandlers.StateQueryHandlersBuilder<UUID, CounterState, CounterFSMState> countingStateQueryHandlersBuilder = StateQueryHandlers.builder();
            countingStateQueryHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            StateQueryHandlers<UUID, CounterState, CounterFSMState> countingStateQueryHandlers = countingStateQueryHandlersBuilder.build();

            final EntityDefinition<UUID, CounterState, CounterFSMState> entityDefinition = EntityDefinition.<UUID, CounterState, CounterFSMState>builder()
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
            final EntityDefinition.EntityDefinitionBuilder<UUID, CounterState, CounterFSMState> entityDefinitionBuilder = EntityDefinition.builder();

            StateCommandHandlers.StateCommandHandlersBuilder<UUID, CounterState, CounterFSMState> stateCommandHandlersBuilder = StateCommandHandlers.builder();
            stateCommandHandlersBuilder.commandHandler(new IncrementCommandHandler());
            stateCommandHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            final StateCommandHandlers<UUID, CounterState, CounterFSMState> countingStateCommandHandlers = stateCommandHandlersBuilder.build();

            StateQueryHandlers.StateQueryHandlersBuilder<UUID, CounterState, CounterFSMState> countingStateQueryHandlersBuilder = StateQueryHandlers.builder();
            countingStateQueryHandlersBuilder.fsmState(CounterFSMState.COUNTING);
            countingStateQueryHandlersBuilder.queryHandler(new GetValueQueryHandler());
            StateQueryHandlers<UUID, CounterState, CounterFSMState> countingStateQueryHandlers = countingStateQueryHandlersBuilder.build();

            final EntityDefinition<UUID, CounterState, CounterFSMState> entityDefinition = entityDefinitionBuilder
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

                Object firstMessageEnvelopeObj = messages.get(0);
                Assert.assertTrue(firstMessageEnvelopeObj instanceof EventEnvelope);

                EventEnvelope firstMessageEnvelope = (EventEnvelope)firstMessageEnvelopeObj;
                Assert.assertNull(firstMessageEnvelope.getNewFSMState());

                Event firstMessageEvent = firstMessageEnvelope.getEvent();
                Assert.assertTrue(firstMessageEvent instanceof ValueChangedEvent);
                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessageEvent;
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

                Object firstMessageObj = messages.get(0);
                Assert.assertTrue(firstMessageObj instanceof EventEnvelope);

                EventEnvelope firstMessageEventEnvelope = (EventEnvelope) firstMessageObj;
                Assert.assertNull(firstMessageEventEnvelope.getNewFSMState());

                Event firstMessageEvent = firstMessageEventEnvelope.getEvent();

                Assert.assertTrue(firstMessageEvent instanceof ValueChangedEvent);
                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessageEvent;
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

                final EventEnvelope<UUID, FSMState> eventEnvelope = getEventEnvelope(this);

                Assert.assertNull(eventEnvelope.getNewFSMState());
                final ValueChangedEvent valueChangedEvent = getEvent(eventEnvelope, ValueChangedEvent.class);
                Assert.assertEquals(uuid, valueChangedEvent.getAggregateId());
                Assert.assertEquals(3, valueChangedEvent.getNewValue());
            }

            {
                GetValueQuery getValueQuery = GetValueQuery
                        .builder()
                        .aggregateId(uuid)
                        .build();
                aggregateRef.tell(getValueQuery, probe);

                final List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                final Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueResult);

                final ValueResult valueResult = (ValueResult) firstMessage;
                Assert.assertEquals(3, valueResult.getValue());
            }
        }};
    }

    protected <U, F extends FSMState> EventEnvelope<U, F> getEventEnvelope(TestKit testKit){
        return getResponse(testKit, EventEnvelope.class);
    }

    protected <U, M extends Message<U>> M getResponse(TestKit testKit, Class<M> messageClass){
        final List<Object> messages = testKit.receiveN(1, duration("1 second"));
        Assert.assertEquals(1, messages.size());

        final Object firstMessage = messages.get(0);

        Assert.assertTrue(messageClass.isInstance(firstMessage));
        return (M)firstMessage;
    }

    protected <U, E extends Event<U>, F extends FSMState>  E getEvent(EventEnvelope<U, F> eventEnvelope, Class<E> eventClass){
        final Event<U> event = eventEnvelope.getEvent();

        Assert.assertTrue(eventClass.isInstance(event));
        return (E)event;
    }
}