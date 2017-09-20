package com.cloudcommander.vendor.ddd.akka.actors.counter.queries;

import com.cloudcommander.vendor.ddd.aggregates.queries.Query;

import java.util.UUID;

public class GetValueQuery implements Query{

    private UUID uuid;

    public GetValueQuery(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Object getTargetId() {
        return uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
