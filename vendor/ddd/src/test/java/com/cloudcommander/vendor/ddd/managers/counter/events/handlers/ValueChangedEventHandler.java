package com.cloudcommander.vendor.ddd.managers.counter.events.handlers;

import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.managers.counter.managerevents.CountIncreasedManagerEvent;
import com.cloudcommander.vendor.ddd.managers.counter.state.CounterManagerState;
import com.cloudcommander.vendor.ddd.managers.events.handlers.EventHandler;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;

/**
 * Created by Adrian Tello on 16/10/2017.
 */
public class ValueChangedEventHandler implements EventHandler<ValueChangedEvent, CountIncreasedManagerEvent, CounterManagerState> {
    @Override
    public ManagerEventEnvelope<CountIncreasedManagerEvent> handle(ValueChangedEvent event, CounterManagerState state) {

        long count = state.getCount();
        long newCount = count + 1;

        ManagerEventEnvelope.ManagerEventEnvelopeBuilder builder = ManagerEventEnvelope.builder();
        if(newCount >= 10){
            builder.stateName("finished");
        }else{
            builder.stateName("counting");
        }

        return builder.build();
    }

    @Override
    public Class<CounterManagerState> getStateClass() {
        return CounterManagerState.class;
    }

    @Override
    public Class<ValueChangedEvent> getEventClass() {
        return ValueChangedEvent.class;
    }
}
