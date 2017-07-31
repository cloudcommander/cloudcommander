package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.Message;
import com.cloudcommander.vendor.cqrs.bus.MessageWrapper;
import com.cloudcommander.vendor.cqrs.bus.dispatchers.strategies.UuidGenerationStrategy;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultMessageBusDispatcher<T extends Message> implements LocalMessageBusDispatcher<T>{

    private CopyOnWriteArrayList<OnDispatchListener> onDispatchListeners = new CopyOnWriteArrayList<>();

    private UuidGenerationStrategy uuidGenerationStrategy;

    @Override
    public void addOnDispatchListener(OnDispatchListener<T> onDispatchListener) {
        onDispatchListeners.add(onDispatchListener);
    }

    @Override
    public void removeOnDispatchEventListener(OnDispatchListener<T> onDispatchListener) {
        onDispatchListeners.remove(onDispatchListener);
    }

    @Override
    public Single<MessageWrapper<T>> dispatch(T message) {
        MessageWrapper<T> messageWrapper = wrapMessage(message);

        for(OnDispatchListener onDispatchListener: onDispatchListeners){
            onDispatchListener.onDispatch(messageWrapper);
        }

        //TODO tello errors to observable

        return Single.fromCallable(() ->
                messageWrapper
        );
    }

    private MessageWrapper<T> wrapMessage(T message) {
        MessageWrapper.MessageWrapperBuilder<T> builder = MessageWrapper.builder();
        builder.uuid(getUuidGenerationStrategy().generate());
        builder.message(message);
        builder.created(new Date());

        return builder.build();
    }

    public CopyOnWriteArrayList<OnDispatchListener> getOnDispatchListeners() {
        return onDispatchListeners;
    }

    public void setOnDispatchListeners(CopyOnWriteArrayList<OnDispatchListener> onDispatchListeners) {
        this.onDispatchListeners = onDispatchListeners;
    }

    protected UuidGenerationStrategy getUuidGenerationStrategy() {
        return uuidGenerationStrategy;
    }

    @Required
    public void setUuidGenerationStrategy(UuidGenerationStrategy uuidGenerationStrategy) {
        this.uuidGenerationStrategy = uuidGenerationStrategy;
    }
}
