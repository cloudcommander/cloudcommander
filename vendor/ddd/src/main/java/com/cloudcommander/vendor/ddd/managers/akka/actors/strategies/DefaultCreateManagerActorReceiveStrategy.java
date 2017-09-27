package com.cloudcommander.vendor.ddd.managers.akka.actors.strategies;

import akka.actor.AbstractActor;
import akka.japi.Procedure;
import akka.japi.pf.ReceiveBuilder;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.events.handlers.EventHandler;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.logs.ManagerLog;
import com.cloudcommander.vendor.ddd.managers.logs.handlers.ManagerLogHandler;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public class DefaultCreateManagerActorReceiveStrategy<T extends Event, U extends ManagerLog, S extends State> implements CreateManagerActorReceiveStrategy<U, S> {

    private final ManagerDefinition<T, U, S> managerDefinition;

    private final Map<String, StateEventHandlers<T, U, S>> stateEventHandlersMap;

    private final Map<Class<U>, ManagerLogHandler<U, S>> managerLogHandlersMap;

    public DefaultCreateManagerActorReceiveStrategy(final ManagerDefinition<T, U, S> managerDefinition){
        this.managerDefinition = managerDefinition;

        stateEventHandlersMap = createStateEventHandlersMap(managerDefinition.getStateEventHandlers());
        managerLogHandlersMap = createManagerLogHandlersMap(managerDefinition.getManagerLogHandlers());
    }

    protected Map<String,StateEventHandlers<T, U, S>> createStateEventHandlersMap(List<? extends StateEventHandlers<T, U, S>> stateEventHandlers) {
        Map<String, StateEventHandlers<T, U, S>> stateEventHandlersMap = new HashMap<>(stateEventHandlers.size());
        for(StateEventHandlers<T, U, S> stateEventHandler: stateEventHandlers){
            final String stateName = stateEventHandler.getName();

            stateEventHandlersMap.put(stateName, stateEventHandler);
        }
        return stateEventHandlersMap;
    }

    private Map<Class<U>, ManagerLogHandler<U, S>> createManagerLogHandlersMap(List<ManagerLogHandler<U, S>> managerLogHandlers) {
        Map<Class<U>, ManagerLogHandler<U, S>> managerLogHandlersMap = new HashMap<>(managerLogHandlers.size());
        for(ManagerLogHandler<U, S> managerLogHandler: managerLogHandlers){
            Class<U> managerLogClass = managerLogHandler.getLogClass();

            managerLogHandlersMap.put(managerLogClass, managerLogHandler);
        }
        return managerLogHandlersMap;
    }

    @Override
    public AbstractActor.Receive createStateReceive(String stateName, Supplier<S> stateSupplier, BiConsumer<U, Procedure<U>> persistFn, AbstractActor.Receive receiveRecover){
        final StateEventHandlers<T, U, S> stateEventHandler = getStateEventHandler(stateName);

        final List<EventHandler<T, U, S>> eventHandlers = stateEventHandler.getEventHandlers();
        final ReceiveBuilder receiveBuilder = ReceiveBuilder.create();
        for(EventHandler<T, U, S> eventHandler: eventHandlers){
            final Class<T> eventClass = eventHandler.getEventClass();
            receiveBuilder.match(eventClass, evt -> {
                S state = stateSupplier.get();
                U managerLog = eventHandler.handle(evt, state);

                persistFn.accept(managerLog, mgrEvt -> {
                    receiveRecover.onMessage().apply(mgrEvt);
                });
            });
        }
        return receiveBuilder.build();
    }

    protected StateEventHandlers<T, U, S> getStateEventHandler(String stateName){
        StateEventHandlers<T, U, S> stateEventHandler = stateEventHandlersMap.get(stateName);

        //TODO tello exception

        return stateEventHandler;
    }

    @Override
    public AbstractActor.Receive createReceiveRecover(Supplier<S> stateSupplier) {
        List<ManagerLogHandler<U, S>> managerLogHandlers = managerDefinition.getManagerLogHandlers();

        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();
        for(ManagerLogHandler<U, S> managerLogHandler: managerLogHandlers){
            Class<U> logClass = managerLogHandler.getLogClass();
            receiveBuilder.match(logClass, log -> {
                S state = stateSupplier.get();

                managerLogHandler.handle(log, state);
            });
        }

        return receiveBuilder.build();
    }
}
