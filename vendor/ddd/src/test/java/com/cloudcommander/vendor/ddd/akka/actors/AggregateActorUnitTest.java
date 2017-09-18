package com.cloudcommander.vendor.ddd.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.javadsl.TestKit;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.DefaultAggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.contexts.DefaultBoundedContextDefinition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

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

    private class TestAggregateState implements AggregateState{
        public TestAggregateState(){

        }
    }

    private class TestAggregateStateFactory implements AggregateStateFactory{

        @Override
        public AggregateState create() {
            return new TestAggregateState();
        }
    }
}