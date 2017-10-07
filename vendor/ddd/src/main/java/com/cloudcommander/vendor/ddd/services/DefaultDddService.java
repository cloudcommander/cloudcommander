package com.cloudcommander.vendor.ddd.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.AggregateRouter;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import java.util.concurrent.CompletionStage;

public class DefaultDddService implements DddService{

    private Timeout timeout;

    private ActorSystem actorSystem;

    private ActorRef aggregateRouterRef;

    public DefaultDddService(ActorSystem actorSystem, Timeout timeout, AggregateDefinition aggregateDefinition) {
        this.timeout = timeout;
        this.actorSystem = actorSystem;

        String name = aggregateDefinition.getName();
        String boundedCtxName = aggregateDefinition.getBoundedContextDefinition().getName();
        String routerPath = boundedCtxName + "--" + name;

        aggregateRouterRef = actorSystem.actorOf(AggregateRouter.props(aggregateDefinition), routerPath);

    }

    @Override
    public void dispatchAndForget(Command command){
        aggregateRouterRef.tell(command, ActorRef.noSender());
    }

    @Override
    public CompletionStage<Object> dispatch(Command command){
        return dispatch(command, timeout);
    }

    @Override
    public CompletionStage<Object> dispatch(Command command, Timeout timeout) {
        Future<Object> future = Patterns.ask(aggregateRouterRef, command, timeout);

        return FutureConverters.toJava(future);
    }

    protected ActorRef getAggregateRegion() {
        return aggregateRouterRef;
    }

    protected ActorSystem getActorSystem() {
        return actorSystem;
    }

    public Timeout getTimeout() {
        return timeout;
    }
}

