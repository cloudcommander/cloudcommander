package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.entities.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

@Builder
@EqualsAndHashCode
@NonFinal
@Value
@Wither
public class CounterState implements State {
    private long value;
}
