package com.cloudcommander.vendor.ddd.aggregates.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AggregateRouter extends AbstractActor {

    private HashMap<Object, ActorRef>  aggregateRefs = new LinkedHashMap<>();

    private AggregateDefinition aggregateDefinition;

    public AggregateRouter(AggregateDefinition aggregateDefinition){
        this.aggregateDefinition = aggregateDefinition;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Command.class, this::handleCommand)
                .build();
    }

    protected void handleCommand(Command command){
        Object targetId = command.getAggregateId();

        ActorRef aggregateRef = aggregateRefs.get(targetId);
        if(aggregateRef == null){
            aggregateRef = createAggregate(targetId);
            aggregateRefs.put(targetId, aggregateRef);
        }

        aggregateRef.forward(command, getContext());
    }

    protected ActorRef createAggregate(Object targetId){
        return getContext().actorOf(AggregateActor.props(aggregateDefinition), targetId.toString());
    }

    public static Props props(final AggregateDefinition aggregateDefinition) {
        return Props.create(AggregateRouter.class, () -> new AggregateRouter(aggregateDefinition));
    }
}
