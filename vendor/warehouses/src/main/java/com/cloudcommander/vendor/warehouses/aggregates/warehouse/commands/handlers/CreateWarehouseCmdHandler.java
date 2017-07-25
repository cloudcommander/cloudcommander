package com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.handlers;

import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.CreateWarehouseCmd;

public interface CreateWarehouseCmdHandler extends CommandHandler<CreateWarehouseCmd, WarehouseState>{
}
