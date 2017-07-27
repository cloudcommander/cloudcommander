package com.cloudcommander.vendor.cqrs.bus.consumers;

import com.cloudcommander.vendor.cqrs.Message;
import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

public class DefaultMessageBusConsumer<T extends Message> implements MessageBusConsumer<T>{

    private Publisher publisher;

    public DefaultMessageBusConsumer(LocalMessageBusDispatcher<T> commandBusDispatcher){
        publisher = Flowable.create((subscriber -> {
            LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener = subscriber::onNext;

            commandBusDispatcher.addOnDispatchListener(onDispatchListener);

            subscriber.setCancellable(() -> commandBusDispatcher.removeOnDispatchEventListener(onDispatchListener));
        }), BackpressureStrategy.BUFFER);
    }

    @Override
    public Publisher<T> getPublisher() {
        return publisher;
    }
}
