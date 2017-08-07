package com.cloudcommander.vendor.cqrs.bus.consumers;

import com.cloudcommander.vendor.cqrs.Message;
import com.cloudcommander.vendor.cqrs.bus.BusMessage;
import org.reactivestreams.Publisher;

public interface MessageBusConsumer<T extends BusMessage> {
    Publisher<T> getPublisher();
}
