package com.cloudcommander.vendor.ddd.managers.akka.actors.strategies;

import akka.actor.AbstractActor;
import akka.japi.Procedure;
import akka.japi.pf.FI;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public interface CreateManagerActorReceiveStrategy <U extends ManagerEvent, S extends State> {
    AbstractActor.Receive createStateReceive(String stateName, Supplier<S> stateSupplier, BiConsumer<U, Procedure<U>> persistFn, AbstractActor.Receive receiveRecover, final FI.UnitApply<Object> sendFunc);

    AbstractActor.Receive createReceiveRecover(Supplier<S> stateSupplier);
}
