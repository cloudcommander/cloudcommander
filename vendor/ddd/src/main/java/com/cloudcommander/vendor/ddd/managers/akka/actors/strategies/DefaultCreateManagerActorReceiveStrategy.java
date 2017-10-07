package com.cloudcommander.vendor.ddd.managers.akka.actors.strategies;

import akka.actor.AbstractActor;
import akka.japi.Procedure;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.events.handlers.EventHandler;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.managerlogs.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.managerlogs.handlers.ManagerEventHandler;
import com.cloudcommander.vendor.ddd.managers.responses.UnhandledEventResponse;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public class DefaultCreateManagerActorReceiveStrategy<T extends Event, U extends ManagerEvent, S extends State> implements CreateManagerActorReceiveStrategy<U, S> {

    private final ManagerDefinition<T, U, S> managerDefinition;

    private final Map<String, StateEventHandlers<T, U, S>> stateEventHandlersMap;

    private final Map<Class<U>, ManagerEventHandler<U, S>> managerEventHandlersMap;

    public DefaultCreateManagerActorReceiveStrategy(final ManagerDefinition<T, U, S> managerDefinition){
        this.managerDefinition = managerDefinition;

        stateEventHandlersMap = createStateEventHandlersMap(managerDefinition.getStateEventHandlers());
        managerEventHandlersMap = createManagerEventHandlersMap(managerDefinition.getManagerEventHandlers());
    }

    protected Map<String,StateEventHandlers<T, U, S>> createStateEventHandlersMap(List<? extends StateEventHandlers<T, U, S>> stateEventHandlers) {
        Map<String, StateEventHandlers<T, U, S>> stateEventHandlersMap = new HashMap<>(stateEventHandlers.size());
        for(StateEventHandlers<T, U, S> stateEventHandler: stateEventHandlers){
            final String stateName = stateEventHandler.getName();

            stateEventHandlersMap.put(stateName, stateEventHandler);
        }
        return stateEventHandlersMap;
    }

    private Map<Class<U>, ManagerEventHandler<U, S>> createManagerEventHandlersMap(List<ManagerEventHandler<U, S>> managerEventHandlers) {
        Map<Class<U>, ManagerEventHandler<U, S>> managerEventHandlersMap = new HashMap<>(managerEventHandlers.size());
        for(ManagerEventHandler<U, S> managerEventHandler : managerEventHandlers){
            Class<U> managerEventClass = managerEventHandler.getEventClass();

            managerEventHandlersMap.put(managerEventClass, managerEventHandler);
        }
        return managerEventHandlersMap;
    }

    @Override
    public AbstractActor.Receive createStateReceive(String stateName, Supplier<S> stateSupplier, BiConsumer<U, Procedure<U>> persistFn, AbstractActor.Receive receiveRecover, final FI.UnitApply<Object> sendFunc){
        final StateEventHandlers<T, U, S> stateEventHandler = getStateEventHandler(stateName);

        final List<EventHandler<T, U, S>> eventHandlers = stateEventHandler.getEventHandlers();
        final ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Dynamic handlers
        for(EventHandler<T, U, S> eventHandler: eventHandlers){
            final Class<T> eventClass = eventHandler.getEventClass();
            receiveBuilder.match(eventClass, evt -> {
                S state = stateSupplier.get();
                U managerEvent = eventHandler.handle(evt, state);

                persistFn.accept(managerEvent, mgrEvt -> {
                    receiveRecover.onMessage().apply(mgrEvt);
                });
            });
        }

        //Fallback
        receiveBuilder.matchAny(event -> {
            UnhandledEventResponse unhandledEventResponse = new UnhandledEventResponse(event.getClass());

            sendFunc.apply(unhandledEventResponse);
        });

        return receiveBuilder.build();
    }

    protected StateEventHandlers<T, U, S> getStateEventHandler(String stateName){
        StateEventHandlers<T, U, S> stateEventHandler = stateEventHandlersMap.get(stateName);

        if(stateEventHandler == null){
            final String boundedContextName = managerDefinition.getBoundedContextDefinition().getName();
            final String managerName = managerDefinition.getName();

            throw new IllegalArgumentException("State with name \"" + stateName + "\" not defined for the manager \""+boundedContextName + "-"
                    + managerName + "\"");
        }

        return stateEventHandler;
    }

    @Override
    public AbstractActor.Receive createReceiveRecover(Supplier<S> stateSupplier) {
        List<ManagerEventHandler<U, S>> managerEventHandlers = managerDefinition.getManagerEventHandlers();

        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();
        for(ManagerEventHandler<U, S> managerEventHandler : managerEventHandlers){
            Class<U> eventClass = managerEventHandler.getEventClass();
            receiveBuilder.match(eventClass, log -> {
                S state = stateSupplier.get();

                managerEventHandler.handle(log, state);
            });
        }

        return receiveBuilder.build();
    }
}
