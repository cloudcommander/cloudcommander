package com.cloudcommander.vendor.ddd.akka.actors.counter.events.handers;

import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterState;

public class ValueChangedEventHandler implements EventHandler<ValueChangedEvent, CounterState> {
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
