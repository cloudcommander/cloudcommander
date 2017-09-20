package com.cloudcommander.vendor.ddd.akka.actors.counter.events.handers;

import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.CounterValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterState;

public class CounterValueChangedEventHandler implements EventHandler<CounterValueChangedEvent, CounterState> {
    @Override
    public void handle(CounterValueChangedEvent event, CounterState state) {
        long newValue = event.getNewValue();
        state.setValue(newValue);
    }

    @Override
    public Class<CounterState> getStateClass() {
        return CounterState.class;
    }

    @Override
    public Class<CounterValueChangedEvent> getEventClass() {
        return CounterValueChangedEvent.class;
    }
}
