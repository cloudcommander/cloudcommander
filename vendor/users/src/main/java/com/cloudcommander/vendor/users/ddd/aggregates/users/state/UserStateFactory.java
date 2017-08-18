package com.cloudcommander.vendor.users.ddd.aggregates.users.state;

import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;

public interface UserStateFactory<T extends UserState> extends AggregateStateFactory<T> {
}
