package com.cloudcommander.vendor.ddd.akka.actors.counter.events.handers;

import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterState;

public class ValueChangedEventHandler implements EventHandler<ValueChangedEvent, CounterState> {
    @Override
    public void handle(ValueChangedEvent event, CounterState state) {
        long newValue = event.getNewValue();
        state.setValue(newValue);
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
