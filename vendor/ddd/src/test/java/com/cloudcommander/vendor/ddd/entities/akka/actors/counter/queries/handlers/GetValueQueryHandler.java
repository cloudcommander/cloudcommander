package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.handlers;

import com.cloudcommander.vendor.ddd.entities.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.queries.GetValueQuery;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.results.ValueResult;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterState;

import java.util.UUID;

public class GetValueQueryHandler implements QueryHandler<UUID, GetValueQuery, ValueResult, CounterState>{
    @Override
    public ValueResult handle(GetValueQuery query, CounterState state) {

        return ValueResult.builder()
                .aggregateId(query.getAggregateId())
                .value(state.getValue())
                .build();
    }

    @Override
    public Class<CounterState> getStateClass() {
        return CounterState.class;
    }

    @Override
    public Class<GetValueQuery> getQueryClass() {
        return GetValueQuery.class;
    }
}
