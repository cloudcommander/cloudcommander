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
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;
import com.cloudcommander.vendor.ddd.akka.actors.counter.commands.handlers.IncrementCommandHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.CounterValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.handers.CounterValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterAggregateStateFactory;
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

    private BoundedContextDefinition boundedContextDefinition = new DefaultBoundedContextDefinition("Users");

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
            String aggregateName = "User";
            AggregateStateFactory aggregateStateFactory = new TestAggregateStateFactory();
            AggregateDefinition aggregateDefinition = new DefaultAggregateDefinition(aggregateName, boundedContextDefinition, aggregateStateFactory, Collections.emptyList(), Collections.emptyList());

            final ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition));
            final ActorRef probe = getRef();

            aggregateRef.tell(new TestCommand1("123"), probe);
            expectMsgClass(UnhandledCommandResponse.class);
        }};
    }

    @Test
    public void testCounterAggregate(){
        new TestKit(system) {{
            String aggregateName = "Counter";
            AggregateStateFactory aggregateStateFactory = new CounterAggregateStateFactory();
            BoundedContextDefinition counterBoundedContextDefinition = new DefaultBoundedContextDefinition("Counter");

            List<CommandHandler<? extends Command, ? extends AggregateState>> commandHandlers = Collections.singletonList(new IncrementCommandHandler());
            List<EventHandler<? extends Event, ? extends AggregateState>> eventHandlers = Collections.singletonList(new CounterValueChangedEventHandler());
            AggregateDefinition aggregateDefinition = new DefaultAggregateDefinition(aggregateName, counterBoundedContextDefinition, aggregateStateFactory, commandHandlers, eventHandlers);

            ActorRef aggregateRef = system.actorOf(AggregateActor.props(aggregateDefinition), "testCounter");
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();

            {
                aggregateRef.tell(new IncrementCommand(uuid), probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof CounterValueChangedEvent);

                CounterValueChangedEvent counterValueChangedEvent = (CounterValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, counterValueChangedEvent.getCounterUuid());
                Assert.assertEquals(1, counterValueChangedEvent.getNewValue());
            }

            {
                aggregateRef.tell(new IncrementCommand(uuid), probe);

                List<Object> messages = receiveN(1, duration("1 second"));
                Assert.assertEquals(1, messages.size());

                Object firstMessage = messages.get(0);
                Assert.assertTrue(firstMessage instanceof CounterValueChangedEvent);

                CounterValueChangedEvent counterValueChangedEvent = (CounterValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, counterValueChangedEvent.getCounterUuid());
                Assert.assertEquals(2, counterValueChangedEvent.getNewValue());
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
                Assert.assertTrue(firstMessage instanceof CounterValueChangedEvent);

                CounterValueChangedEvent counterValueChangedEvent = (CounterValueChangedEvent) firstMessage;
                Assert.assertEquals(uuid, counterValueChangedEvent.getCounterUuid());
                Assert.assertEquals(3, counterValueChangedEvent.getNewValue());
            }
        }};
    }

    private static class TestCommand1 implements Command{

        private String id;

        public TestCommand1(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        @Override
        public Object getTargetId() {
            return "";
        }
    }

    private static class TestAggregateState implements AggregateState{
        public TestAggregateState(){

        }
    }

    private static class TestAggregateStateFactory implements AggregateStateFactory{

        @Override
        public AggregateState create() {
            return new TestAggregateState();
        }
    }
}