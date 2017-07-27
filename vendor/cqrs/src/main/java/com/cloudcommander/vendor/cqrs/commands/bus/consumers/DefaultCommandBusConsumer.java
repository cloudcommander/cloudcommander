package com.cloudcommander.vendor.cqrs.commands.bus.consumers;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.bus.dispatchers.DefaultCommandBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.bus.dispatchers.LocalCommandBusDispatcher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;


public class DefaultCommandBusConsumer<T extends Command> implements CommandBusConsumer<T> {

    private Publisher publisher;

    public DefaultCommandBusConsumer(LocalCommandBusDispatcher commandBusDispatcher){
        publisher = Flowable.create((subscriber -> {
            DefaultCommandBusDispatcher.OnDispatchListener<T> onDispatchListener = subscriber::onNext;

            commandBusDispatcher.addOnDispatchListener(onDispatchListener);

            subscriber.setCancellable(() -> commandBusDispatcher.removeOnDispatchEventListener(onDispatchListener));
        }), BackpressureStrategy.BUFFER);
    }

    @Override
    public Publisher<T> getPublisher() {
        return publisher;
    }
}
