package com.cloudcommander.vendor.ddd.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.akka.actors.AggregateActor;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import java.util.concurrent.CompletionStage;

public class DefaultDddService implements DddService{

    private Timeout timeout;

    private ActorSystem actorSystem;

    private ActorRef aggregateRegion;

    public DefaultDddService(ActorSystem actorSystem, ShardRegion.MessageExtractor shardMessageExtractor, Timeout timeout, AggregateDefinition aggregateDefinition) {
        this.timeout = timeout;
        this.actorSystem = actorSystem;

        String name = aggregateDefinition.getName();
        String boundedCtxName = aggregateDefinition.getBoundedContextDefinition().getName();
        String regionName = boundedCtxName + "--" + name;

        ClusterShardingSettings settings = ClusterShardingSettings.create(actorSystem);
        settings.withStateStoreMode("persistence");
        aggregateRegion = ClusterSharding.get(actorSystem).start(regionName,
                Props.create(AggregateActor.class, aggregateDefinition), settings, shardMessageExtractor);

    }

    @Override
    public void dispatchAndForget(Command command){
        aggregateRegion.tell(command, ActorRef.noSender());
    }

    @Override
    public CompletionStage<Object> dispatch(Command command){
        return dispatch(command, timeout);
    }

    @Override
    public CompletionStage<Object> dispatch(Command command, Timeout timeout) {
        Future<Object> future = Patterns.ask(aggregateRegion, command, timeout);

        return FutureConverters.toJava(future);
    }

    protected ActorRef getAggregateRegion() {
        return aggregateRegion;
    }

    protected ActorSystem getActorSystem() {
        return actorSystem;
    }

    public Timeout getTimeout() {
        return timeout;
    }
}

