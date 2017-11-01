package com.cloudcommander.vendor.ddd.entities.events;

import com.cloudcommander.vendor.ddd.entities.states.State;

public interface EventHandler <U, E extends Event<U>, S extends State, EC extends E> {
     S handle(EC event, S state);

    Class<S> getStateClass();

    Class<EC> getEventClass();
}
