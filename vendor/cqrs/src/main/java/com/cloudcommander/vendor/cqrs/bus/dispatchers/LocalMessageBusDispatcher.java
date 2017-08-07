package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.BusMessage;

public interface LocalMessageBusDispatcher<T extends BusMessage> extends MessageBusDispatcher<T>{
    void addOnDispatchListener(LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener);

    void removeOnDispatchEventListener(LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener);

    interface OnDispatchListener<T>{
        void onDispatch(T command);
    }
}
