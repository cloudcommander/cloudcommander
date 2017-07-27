package com.cloudcommander.vendor.cqrs.bus.consumers;

import com.cloudcommander.vendor.cqrs.Message;
import org.reactivestreams.Publisher;

public interface MessageBusConsumer<T extends Message> {
    Publisher<T> getPublisher();
}
