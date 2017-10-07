package com.cloudcommander.vendor.ddd.managers.akka.actors;

import akka.actor.Props;
import akka.persistence.AbstractPersistentActor;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.akka.actors.strategies.CreateManagerActorReceiveStrategy;
import com.cloudcommander.vendor.ddd.managers.managerlogs.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
public class ManagerActor <T extends Event, U extends ManagerEvent, S extends State> extends AbstractPersistentActor {

    private ManagerDefinition<T, U, S> managerDefinition;

    private Map<String, Receive> stateReceivers;

    private CreateManagerActorReceiveStrategy<U, S> createReceiveStrategy;

    private S state;

    private Receive receiveRecover;

    public ManagerActor(ManagerDefinition<T, U, S> managerDefinition, CreateManagerActorReceiveStrategy<U, S> createReceiveStrategy){
        this.managerDefinition = managerDefinition;

        stateReceivers = new HashMap<>();
        this.createReceiveStrategy = createReceiveStrategy;

        receiveRecover = createReceiveStrategy.createReceiveRecover(() -> this.state);
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveRecover;
    }

    @Override
    public Receive createReceive() {
        String startStateName = managerDefinition.getStartStateName();

        return getOrCreateStateReceive(startStateName);
    }

    private Receive getOrCreateStateReceive(String stateName){
        Receive receive = stateReceivers.get(stateName);

        if(receive == null) {
            receive = createReceiveStrategy.createStateReceive(stateName, () -> this.state, this::persist, receiveRecover, msgToSend -> {
                getSender().tell(msgToSend, getSelf());
            });
        }

        return receive;
    }

    @Override
    public String persistenceId() {
        String managerName = managerDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = managerDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + managerName + '-' + getSelf().path().name() + "-Mgr";
    }

    public static Props props(final ManagerDefinition<Event, ManagerEvent, com.cloudcommander.vendor.ddd.managers.states.State> managerDefinition, CreateManagerActorReceiveStrategy<ManagerEvent, com.cloudcommander.vendor.ddd.managers.states.State> createManagerActorReceiveStrategy) {
        return Props.create(ManagerActor.class, () -> new ManagerActor<Event, ManagerEvent, com.cloudcommander.vendor.ddd.managers.states.State>(managerDefinition, createManagerActorReceiveStrategy));
    }
}
