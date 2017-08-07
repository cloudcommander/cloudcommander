package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.BusMessage;

import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultMessageBusDispatcher<T extends BusMessage> implements LocalMessageBusDispatcher<T>{

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

    public CopyOnWriteArrayList<OnDispatchListener> getOnDispatchListeners() {
        return onDispatchListeners;
    }

    public void setOnDispatchListeners(CopyOnWriteArrayList<OnDispatchListener> onDispatchListeners) {
        this.onDispatchListeners = onDispatchListeners;
    }
}
