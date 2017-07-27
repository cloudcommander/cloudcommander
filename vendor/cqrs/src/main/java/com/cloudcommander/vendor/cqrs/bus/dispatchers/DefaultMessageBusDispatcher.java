package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.Message;

import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultMessageBusDispatcher<T extends Message> implements LocalMessageBusDispatcher<T>{

    private CopyOnWriteArrayList<OnDispatchListener> onDispatchListeners = new CopyOnWriteArrayList<>();

    @Override
    public void addOnDispatchListener(OnDispatchListener<T> onDispatchListener) {
        onDispatchListeners.add(onDispatchListener);
    }

    @Override
    public void removeOnDispatchEventListener(OnDispatchListener<T> onDispatchListener) {
        onDispatchListeners.remove(onDispatchListener);
    }

    @Override
    public void dispatch(T message) {
        for(OnDispatchListener onDispatchListener: onDispatchListeners){
            onDispatchListener.onDispatch(message);
        }
    }
}
