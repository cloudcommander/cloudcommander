package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.fsmstates.CounterFSMState;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterState;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.StateFactory;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterStateFactory;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import org.junit.AfterClass;
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
            final StateCommandHandlers<UUID, Command<UUID>, Event<UUID>, CounterState, CounterFSMState> countingStateCommandHandlers = new StateCommandHandlers<>(CounterFSMState.COUNTING, Collections.emptyList());
            final List<? extends StateCommandHandlers<UUID, Command<UUID>, Event<UUID>, CounterState, CounterFSMState>> stateCommandHandlers = Collections.singletonList(countingStateCommandHandlers);

            final Map<Event<UUID>, ? extends EventHandler<UUID, Event<UUID>, CounterState>> eventHandlerMap = Collections.emptyMap();

            final StateQueryHandlers<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> countingStateQueryHandlers = new StateQueryHandlers<>(CounterFSMState.COUNTING, Collections.emptyList());
            final List<? extends StateQueryHandlers<UUID, Query<UUID>, Result<UUID>, CounterState, CounterFSMState>> stateQueryHandlers = Collections.singletonList(countingStateQueryHandlers);

            final EntityDefinition<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState> entityDefinition = EntityDefinition.<UUID, Command<UUID>, Event<UUID>, Query<UUID>, Result<UUID>, CounterState, CounterFSMState>builder()
                    .name("Counter")
                    .boundedContextDefinition(counterBoundedContextDefinition)
                    .stateFactory(stateFactory)
                    .stateCommandHandlersList(stateCommandHandlers)
                    .eventHandlerMap(eventHandlerMap)
                    .stateQueryHandlersList(stateQueryHandlers)
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
}