package com.cloudcommander.vendor.cqrs.bus.consumers;

import com.cloudcommander.vendor.cqrs.Message;
import com.cloudcommander.vendor.cqrs.bus.BusMessage;
import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

public class DefaultMessageBusConsumer<T extends BusMessage> implements MessageBusConsumer<T>{

    private Publisher publisher;

    public DefaultMessageBusConsumer(LocalMessageBusDispatcher<T> messageBusDispatcher){
        publisher = Flowable.create((subscriber -> {
            LocalMessageBusDispatcher.OnDispatchListener<T> onDispatchListener = subscriber::onNext;

            messageBusDispatcher.addOnDispatchListener(onDispatchListener);

            subscriber.setCancellable(() -> messageBusDispatcher.removeOnDispatchEventListener(onDispatchListener));
        }), BackpressureStrategy.BUFFER);
    }

    @Override
    public Publisher<T> getPublisher() {
        return publisher;
    }
}
