package com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.handlers;

import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.commands.CreateWarehouseCmd;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreatedEvt;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreationFailedEvt;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DefaultCreateWarehouseCmdHandler implements CreateWarehouseCmdHandler {
    @Override
    public List<Event> handle(CreateWarehouseCmd command, Optional<WarehouseState> currentStateOptional) {
        if(currentStateOptional.isPresent()){
            return handle(command, currentStateOptional.get());
        }else{
            return handle(command);
        }
    }

    protected List<Event> handle(CreateWarehouseCmd command, WarehouseState currentState) {
        WarehouseCreationFailedEvt.WarehouseCreationFailedEvtBuilder eventBuilder = WarehouseCreationFailedEvt.builder();
        eventBuilder.code(command.getCode());
        eventBuilder.name(command.getName());

        WarehouseCreationFailedEvt event = eventBuilder.build();
        return Collections.singletonList(event);
    }

    protected List<Event> handle(CreateWarehouseCmd command) {
        WarehouseCreatedEvt.WarehouseCreatedEvtBuilder eventBuilder = WarehouseCreatedEvt.builder();
        eventBuilder.code(command.getCode());
        eventBuilder.name(command.getName());

        WarehouseCreatedEvt event = eventBuilder.build();

        return Collections.singletonList(event);
    }

    @Override
    public Class<CreateWarehouseCmd> getCommandClass() {
        return CreateWarehouseCmd.class;
    }
}
