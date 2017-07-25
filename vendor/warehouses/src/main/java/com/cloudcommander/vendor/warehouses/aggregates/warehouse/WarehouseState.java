package com.cloudcommander.vendor.warehouses.aggregates.warehouse;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

@Builder
@Data
@Wither
public class WarehouseState {

    @NonNull
    String code;

    @NonNull
    String name;
}
