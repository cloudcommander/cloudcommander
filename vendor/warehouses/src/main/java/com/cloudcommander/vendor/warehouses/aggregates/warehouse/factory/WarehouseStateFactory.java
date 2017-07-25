package com.cloudcommander.vendor.warehouses.aggregates.warehouse.factory;

import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.CreateWarehouseCmd;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreatedEvt;

public interface WarehouseStateFactory {
    WarehouseState create(WarehouseCreatedEvt warehouseCreatedEvt);
}
