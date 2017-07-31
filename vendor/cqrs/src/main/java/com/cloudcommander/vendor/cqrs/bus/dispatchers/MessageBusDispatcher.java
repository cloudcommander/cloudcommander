package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.Message;
import com.cloudcommander.vendor.cqrs.bus.MessageWrapper;
import io.reactivex.Single;

public interface MessageBusDispatcher <T extends Message>{
    Single<MessageWrapper<T>> dispatch(T message);
}
