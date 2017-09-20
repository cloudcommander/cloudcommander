package com.cloudcommander.vendor.users.ddd.aggregates.users.state;

import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;

public interface UserStateFactory<T extends UserState> extends StateFactory<T> {
}
