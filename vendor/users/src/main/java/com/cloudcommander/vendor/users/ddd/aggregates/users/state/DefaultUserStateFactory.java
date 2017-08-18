package com.cloudcommander.vendor.users.ddd.aggregates.users.state;

public class DefaultUserStateFactory implements UserStateFactory<UserState>{
    @Override
    public UserState create() {
        return new DefaultUserState();
    }
}
