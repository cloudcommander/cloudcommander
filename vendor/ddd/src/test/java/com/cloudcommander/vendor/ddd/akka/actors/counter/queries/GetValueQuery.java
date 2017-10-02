package com.cloudcommander.vendor.ddd.akka.actors.counter.queries;

import com.cloudcommander.vendor.ddd.aggregates.queries.Query;

import java.util.UUID;

import org.immutables.value.Value;

@Value.Immutable
public interface GetValueQuery extends Query<UUID>{

}
