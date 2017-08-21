package com.cloudcommander.vendor.ddd.akka.actors;

import akka.actor.UntypedAbstractActor;

public class DddSupervisorActor extends UntypedAbstractActor{
    @Override
    public void onReceive(Object message) throws Throwable {
        unhandled(message);
    }
}
