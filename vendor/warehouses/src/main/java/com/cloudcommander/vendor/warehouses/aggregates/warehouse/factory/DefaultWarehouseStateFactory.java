package com.cloudcommander.vendor.warehouses.aggregates.warehouse.factory;

import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.CreateWarehouseCmd;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreatedEvt;

public class DefaultWarehouseStateFactory implements WarehouseStateFactory {
    @Override
    public WarehouseState create(WarehouseCreatedEvt warehouseCreatedEvt) {
        return WarehouseState.builder()
                .code(warehouseCreatedEvt.getCode())
                .name(warehouseCreatedEvt.getName())
                .build();
    }
}
