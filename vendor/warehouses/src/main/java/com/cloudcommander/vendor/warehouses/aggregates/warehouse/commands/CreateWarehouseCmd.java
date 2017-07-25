package com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands;

import com.cloudcommander.vendor.cqrs.commands.Command;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Builder
@Value
@Wither
public class CreateWarehouseCmd extends Command {
    String code;

    String name;
}
