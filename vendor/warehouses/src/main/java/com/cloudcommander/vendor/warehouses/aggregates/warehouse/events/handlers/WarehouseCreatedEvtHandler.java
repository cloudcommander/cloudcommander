package com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.handlers;

import com.cloudcommander.vendor.cqrs.events.EventHandler;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreatedEvt;

public interface WarehouseCreatedEvtHandler extends EventHandler<WarehouseCreatedEvt, WarehouseState>{
}
