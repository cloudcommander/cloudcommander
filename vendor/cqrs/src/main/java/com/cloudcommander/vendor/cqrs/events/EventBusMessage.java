package com.cloudcommander.vendor.cqrs.events;

import com.cloudcommander.vendor.cqrs.bus.BusMessage;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

@Builder
@Data
@Wither
public class EventBusMessage <T extends Event> extends BusMessage {
    T event;
}
