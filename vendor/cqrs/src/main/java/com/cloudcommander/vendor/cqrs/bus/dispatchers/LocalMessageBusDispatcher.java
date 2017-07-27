package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.Message;

public interface LocalMessageBusDispatcher<T extends Message> extends MessageBusDispatcher<T>{
    void addOnDispatchListener(LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener);

    void removeOnDispatchEventListener(LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener);

    interface OnDispatchListener<T>{
        void onDispatch(T command);
    }
}
