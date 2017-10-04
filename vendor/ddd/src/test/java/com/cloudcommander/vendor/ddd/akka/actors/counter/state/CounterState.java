package com.cloudcommander.vendor.ddd.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

@Builder
@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
@Wither
public class CounterState extends State {
    private long value;
}
