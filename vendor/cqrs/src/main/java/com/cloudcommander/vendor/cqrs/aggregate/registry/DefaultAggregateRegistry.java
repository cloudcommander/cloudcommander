package com.cloudcommander.vendor.cqrs.aggregate.registry;

import com.cloudcommander.vendor.cqrs.aggregate.AggregateConfig;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultAggregateRegistry implements AggregateRegistry{
    private List<AggregateConfig> aggregateConfigs;

    protected List<AggregateConfig> getAggregateConfigs() {
        return aggregateConfigs;
    }

    @Autowired
    @Required
    public void setAggregateConfigs(List<AggregateConfig> aggregateConfigs) {
        this.aggregateConfigs = ImmutableList.copyOf(aggregateConfigs);
    }
}
