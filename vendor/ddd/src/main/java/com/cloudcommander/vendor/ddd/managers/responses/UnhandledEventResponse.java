package com.cloudcommander.vendor.ddd.managers.responses;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public class UnhandledEventResponse {
    private Class<? extends Object> eventClass;

    public UnhandledEventResponse(Class<? extends Object> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends Object> getEventClass() {
        return eventClass;
    }
}
