package com.cloudcommander.vendor.cqrs.aggregates.registry;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateConfig;

import java.util.List;

public interface AggregateRegistry {
    List<AggregateConfig> getAggregateConfigs();
}
