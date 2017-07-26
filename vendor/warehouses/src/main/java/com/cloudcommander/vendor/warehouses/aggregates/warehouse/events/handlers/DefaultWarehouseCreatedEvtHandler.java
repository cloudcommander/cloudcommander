package com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.handlers;

import com.cloudcommander.vendor.warehouses.aggregates.warehouse.WarehouseState;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.events.WarehouseCreatedEvt;
import com.cloudcommander.vendor.warehouses.aggregates.warehouse.factory.WarehouseStateFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Optional;

public class DefaultWarehouseCreatedEvtHandler implements WarehouseCreatedEvtHandler{

    private WarehouseStateFactory warehouseStateFactory;

    @Override
    public Optional<WarehouseState> handle(WarehouseCreatedEvt event, Optional<WarehouseState> currentStateOptional) {
        WarehouseState state = getWarehouseStateFactory().create(event);
        return Optional.of(state);
    }

    @Override
    public Class<WarehouseCreatedEvt> getEventClass() {
        return WarehouseCreatedEvt.class;
    }

    protected WarehouseStateFactory getWarehouseStateFactory() {
        return warehouseStateFactory;
    }

    @Required
    public void setWarehouseStateFactory(WarehouseStateFactory warehouseStateFactory) {
        this.warehouseStateFactory = warehouseStateFactory;
    }
}
