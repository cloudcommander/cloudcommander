package com.cloudcommander.vendor.ddd.akka.actors.counter.commands;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface IncrementCommand extends Command<UUID> {

}
