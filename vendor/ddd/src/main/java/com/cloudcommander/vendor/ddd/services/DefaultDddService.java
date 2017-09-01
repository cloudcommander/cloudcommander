package com.cloudcommander.vendor.ddd.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.akka.actors.AggregateActor;

public class DefaultDddService implements DddService{

    private ActorRef aggregateRegion;

    public DefaultDddService(ActorSystem actorSystem, ShardRegion.MessageExtractor shardMessageExtractor, AggregateDefinition aggregateDefinition) {
        String name = aggregateDefinition.getName();
        String boundedCtxName = aggregateDefinition.getBoundedContextDefinition().getName();
        String regionName = boundedCtxName + "--" + name;

        ClusterShardingSettings settings = ClusterShardingSettings.create(actorSystem);
        settings.withStateStoreMode("persistence");
        aggregateRegion = ClusterSharding.get(actorSystem).start(regionName,
                Props.create(AggregateActor.class, aggregateDefinition), settings, shardMessageExtractor);

    }

    protected ActorRef getAggregateRegion() {
        return aggregateRegion;
    }
}

