package com.cloudcommander.vendor.ddd.aggregates.clustering;

import akka.cluster.sharding.ShardRegion;
import com.cloudcommander.vendor.ddd.aggregates.Message;

public class DefaultDddMessageExtractor extends ShardRegion.HashCodeMessageExtractor implements ShardRegion.MessageExtractor {

    public DefaultDddMessageExtractor(int maxNumberOfShards) {
        super(maxNumberOfShards);
    }

    @Override
    public String entityId(Object message) {
        String entityId = null;

        if(message instanceof Message){
            Message castedMessage = (Message)message;
            Object targetId = castedMessage.getTargetId();
            entityId = targetId.toString();
        }

        return entityId;
    }
}