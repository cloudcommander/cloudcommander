package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.events.Event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultCommandBusDispatcher<T extends Command, S extends Event> implements LocalCommandBusDispatcher<T, S>{

    private CopyOnWriteArrayList<OnDispatchListener> onDispatchListeners = new CopyOnWriteArrayList<>();

    @Override
    public void dispatchAndForget(T command) {
        for(OnDispatchListener onDispatchListener: onDispatchListeners){
            onDispatchListener.onDispatch(command);
        }
    }

    @Override
    public List<S> dispatch(T command) {
        return null;//TODO tello implement
    }

    public void addOnDispatchListener(OnDispatchListener<T> onDispatchListener){
        onDispatchListeners.add(onDispatchListener);
    }

    public void removeOnDispatchEventListener(OnDispatchListener<T> onDispatchListener){
        onDispatchListeners.remove(onDispatchListener);
    }

    public static interface OnDispatchListener<T>{
        void onDispatch(T command);
    }
}
