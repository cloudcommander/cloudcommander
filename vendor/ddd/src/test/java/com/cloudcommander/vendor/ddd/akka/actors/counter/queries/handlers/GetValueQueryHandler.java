package com.cloudcommander.vendor.ddd.akka.actors.counter.queries.handlers;

import com.cloudcommander.vendor.ddd.aggregates.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.queries.GetValueQuery;
import com.cloudcommander.vendor.ddd.akka.actors.counter.results.ValueResult;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterState;

public class GetValueQueryHandler implements QueryHandler<GetValueQuery, ValueResult, CounterState>{
    @Override
    public ValueResult handle(GetValueQuery query, CounterState state) {
        return new ValueResult(state.getValue());
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
