package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.handers;

import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state.CounterState;

import java.util.UUID;

public class ValueChangedEventHandler implements EventHandler<UUID, Event<UUID>, CounterState, ValueChangedEvent> {
    @Override
    public CounterState handle(ValueChangedEvent event, CounterState state) {
        return state.withValue(event.getNewValue());
    }

    @Override
    public Class<CounterState> getStateClass() {
        return CounterState.class;
    }

    @Override
    public Class<ValueChangedEvent> getEventClass() {
        return ValueChangedEvent.class;
    }
}
