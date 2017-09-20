package com.cloudcommander.vendor.ddd.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.actor.PoisonPill;
import akka.actor.Terminated;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.DefaultAggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.akka.actors.counter.commands.handlers.IncrementCommandHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.handers.ValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterStateFactory;
import com.cloudcommander.vendor.ddd.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.contexts.DefaultBoundedContextDefinition;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RunWith(JUnit4.class)
public class AggregateActorUnitTest{

    private StateFactory stateFactory = new CounterStateFactory();
    private BoundedContextDefinition counterBoundedContextDefinition = new DefaultBoundedContextDefinition("Counter");

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
            AggregateDefinition aggregateDefinition = new DefaultAggregateDefinition("Counter", counterBoundedContextDefinition, stateFactory, Collections.emptyList(), Collections.emptyList());

            final ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition));
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();
            aggregateRef.tell(new IncrementCommand(uuid), probe);
            expectMsgClass(UnhandledCommandResponse.class);
        }};
    }

    @Test
    public void testCounterAggregate(){
        new TestKit(system) {{
            List<CommandHandler<? extends Command, ? extends State>> commandHandlers = Collections.singletonList(new IncrementCommandHandler());
            List<EventHandler<? extends Event, ? extends State>> eventHandlers = Collections.singletonList(new ValueChangedEventHandler());
            AggregateDefinition aggregateDefinition = new DefaultAggregateDefinition("Counter", counterBoundedContextDefinition, stateFactory, commandHandlers, eventHandlers);

            ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition), "testCounter");
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();

            {
                aggregateRef.tell(new IncrementCommand(uuid), probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getCounterUuid());
                Assert.assertEquals(1, valueChangedEvent.getNewValue());
            }

            {
                aggregateRef.tell(new IncrementCommand(uuid), probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getCounterUuid());
                Assert.assertEquals(2, valueChangedEvent.getNewValue());
            }

            {
                watch(aggregateRef);
                aggregateRef.tell(PoisonPill.getInstance(), probe);
                expectMsgClass(Terminated.class);

                aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition), "testCounter");
                aggregateRef.tell(new IncrementCommand(uuid), probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof ValueChangedEvent);

                ValueChangedEvent valueChangedEvent = (ValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, valueChangedEvent.getCounterUuid());
                Assert.assertEquals(3, valueChangedEvent.getNewValue());
            }
        }};
    }
}