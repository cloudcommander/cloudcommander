package com.cloudcommander.vendor.ddd.akka.actors.counter.results;

import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface ValueResult extends Result<UUID> {
    long getValue();
}
