package com.cloudcommander.vendor.warehouses.aggregates.warehouse;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateState;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

@Builder
@Data
@Wither
public class WarehouseState extends AggregateState{

    @NonNull
    String code;

    @NonNull
    String name;
}
