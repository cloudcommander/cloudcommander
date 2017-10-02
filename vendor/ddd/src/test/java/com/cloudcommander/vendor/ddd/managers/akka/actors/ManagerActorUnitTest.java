package com.cloudcommander.vendor.ddd.managers.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.DefaultAggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.akka.actors.AggregateActor;
import com.cloudcommander.vendor.ddd.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.contexts.DefaultBoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.DefaultManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.akka.actors.strategies.CreateManagerActorReceiveStrategy;
import com.cloudcommander.vendor.ddd.managers.akka.actors.strategies.DefaultCreateManagerActorReceiveStrategy;
import com.cloudcommander.vendor.ddd.managers.events.handlers.DefaultStateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.logs.ManagerLog;
import com.cloudcommander.vendor.ddd.managers.responses.UnhandledEventResponse;
import com.cloudcommander.vendor.ddd.managers.states.State;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ImmutableValueChangedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
@RunWith(JUnit4.class)
public class ManagerActorUnitTest {

    private BoundedContextDefinition boundedContextDefinition = new DefaultBoundedContextDefinition("Counter");

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("ManagerActorUnitTest");
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testNonMappedEvent(){
        new TestKit(system) {{
            StateEventHandlers<Event, ManagerLog, State> stateEventHandlers = new DefaultStateEventHandlers<>("counting", Collections.emptyList());
            ManagerDefinition<Event, ManagerLog, State> managerDefinition = new DefaultManagerDefinition("CounterNotificationMgr", boundedContextDefinition, "counting", Collections.singletonList(stateEventHandlers), Collections.emptyList());
            CreateManagerActorReceiveStrategy<ManagerLog, State> createReceiveStrategy = new DefaultCreateManagerActorReceiveStrategy<>(managerDefinition);

            final ActorRef aggregateRef = system.actorOf(ManagerActor.props(managerDefinition, createReceiveStrategy));
            final ActorRef probe = getRef();

            UUID uuid = UUID.randomUUID();
            ValueChangedEvent event = ImmutableValueChangedEvent.builder()
                    .aggregateId(uuid)
                    .newValue(5)
                    .build();

            aggregateRef.tell(event, probe);
            expectMsgClass(UnhandledEventResponse.class);
        }};
    }
}
