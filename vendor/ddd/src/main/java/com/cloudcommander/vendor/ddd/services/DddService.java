package com.cloudcommander.vendor.ddd.services;

import akka.util.Timeout;
import com.cloudcommander.vendor.ddd.entities.commands.Command;

import java.util.concurrent.CompletionStage;

public interface DddService {

    void dispatchAndForget(Command command);

    CompletionStage<Object> dispatch(Command command);

    CompletionStage<Object> dispatch(Command command, Timeout timeout);
}
