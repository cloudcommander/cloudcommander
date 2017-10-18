package com.cloudcommander.vendor.ddd.managers.counter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.contexts.DefaultBoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.DefaultManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.akka.actors.ManagerActor;
import com.cloudcommander.vendor.ddd.managers.akka.actors.strategies.CreateManagerActorReceiveStrategy;
import com.cloudcommander.vendor.ddd.managers.akka.actors.strategies.DefaultCreateManagerActorReceiveStrategy;
import com.cloudcommander.vendor.ddd.managers.counter.events.handlers.ValueChangedEventHandler;
import com.cloudcommander.vendor.ddd.managers.counter.state.CounterManagerStateFactory;
import com.cloudcommander.vendor.ddd.managers.events.handlers.DefaultStateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.events.handlers.EventHandler;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;

import com.cloudcommander.vendor.ddd.managers.states.ManagerStateFactory;
import com.cloudcommander.vendor.ddd.managers.states.State;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static akka.testkit.JavaTestKit.duration;

/**
 * Created by Adrian Tello on 09/10/2017.
 */
@RunWith(JUnit4.class)
public class CounterManagerUnitTest {

    private final BoundedContextDefinition boundedContextDefinition = new DefaultBoundedContextDefinition("Counter");

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("CounterManagerUnitTest");
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void testSuccess(){
        new TestKit(system) {{

            ManagerStateFactory managerStateFactory = new CounterManagerStateFactory();

            EventHandler<? extends Event, ? extends ManagerEventEnvelope, ? extends State> valueChangedEventHandler = new ValueChangedEventHandler();
            StateEventHandlers<Event, ManagerEventEnvelope, State> coutingStateEventHandlers = new DefaultStateEventHandlers("counting", Collections.singletonList(valueChangedEventHandler));
            ManagerDefinition<Event, ManagerEventEnvelope, State> managerDefinition = new DefaultManagerDefinition("CounterNotificationMgr", boundedContextDefinition, "counting", Collections.singletonList(coutingStateEventHandlers), Collections.emptyList(), managerStateFactory);
            CreateManagerActorReceiveStrategy<ManagerEventEnvelope, State> createReceiveStrategy = new DefaultCreateManagerActorReceiveStrategy<>(managerDefinition);

            final ActorRef aggregateRef = system.actorOf(ManagerActor.props(managerDefinition, createReceiveStrategy));
            final TestKit testKit = this;

            //Test
            for(int i = 0; i < 9 ; i++){
                ManagerEventEnvelope managerEvent = tellValueChangedEvent(i, aggregateRef, testKit);
                Assert.assertEquals("counting", managerEvent.getStateName());
            }

            ManagerEventEnvelope managerEvent = tellValueChangedEvent(10, aggregateRef, testKit);
            Assert.assertEquals("finished", managerEvent.getStateName());
        }};
    }

    private static ManagerEventEnvelope tellValueChangedEvent(long newValue, ActorRef aggregateRef, TestKit testKit){
        ActorRef probe = testKit.getRef();

        ValueChangedEvent event = createValueChangedEvent(newValue);
        aggregateRef.tell(event, probe);

        List<Object> eventList = testKit.receiveN(1, duration("1 second"));

        Object eventObj = eventList.get(0);
        Assert.assertTrue(eventObj instanceof ManagerEventEnvelope);

        return (ManagerEventEnvelope)eventObj;
    }

    private static ValueChangedEvent createValueChangedEvent(long newValue){
        UUID uuid = UUID.randomUUID();

        return ValueChangedEvent.builder()
                .aggregateId(uuid)
                .newValue(newValue)
                .build();
    }

    private void assertStateName(String stateName, ActorRef aggregateRef, TestKit testKit) {
        //TODO
    }
}
