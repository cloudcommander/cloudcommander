package com.cloudcommander.vendor.warehouses.aggregates.warehouse.events;

import com.cloudcommander.vendor.cqrs.events.Event;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Builder
@Value
@Wither
public class WarehouseCreatedEvt extends Event{
    String code;

    String name;
}
