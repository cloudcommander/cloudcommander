package com.cloudcommander.vendor.warehouses.aggregates.warehouse.events;

import com.cloudcommander.vendor.cqrs.aggregate.Event;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Builder
@Value
@Wither
public class WarehouseCreationFailedEvt extends Event{
    String code;

    String name;
    //TODO tello reason
}
