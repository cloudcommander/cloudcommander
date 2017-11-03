package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands;

import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.Converter;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands.dto.CounterActorCommands;

/**
 * Created by Adrian Tello on 03/11/2017.
 */
public interface ProtoIncrementCommandConverter extends Converter<IncrementCommand, CounterActorCommands.IncrementCommand>{
}
