package com.cloudcommander.vendor.cqrs.commands.bus;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateConfig;
import com.cloudcommander.vendor.cqrs.aggregates.registry.AggregateRegistry;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultCommandBusDispatcher implements CommandBusDispatcher {

    private final static Logger LOG = LogManager.getLogger(DefaultCommandBusDispatcher.class);

    protected Map<Class<? extends Command>, CommandHandler> commandHandlerMap = Collections.emptyMap();

    private AggregateRegistry aggregateRegistry;

    @Override
    public void dispatchAndForget(Command command) {
        //TODO tello implement
    }

    @Override
    public List<Event> dispatch(Command command) {
        return null;//TODO tello implement
    }

    protected Map<Class<? extends Command>, CommandHandler> getCommandHandlerMap() {
        return commandHandlerMap;
    }

    protected void setCommandHandlerMap(Map<Class<? extends Command>, CommandHandler> commandHandlerMap){
        this.commandHandlerMap = commandHandlerMap;
    }

    protected AggregateRegistry getAggregateRegistry() {
        return aggregateRegistry;
    }

    @Required
    public void setAggregateRegistry(AggregateRegistry aggregateRegistry) {
        this.aggregateRegistry = aggregateRegistry;

        updateCommandHandlerMap();
    }

    protected void updateCommandHandlerMap() {
        List<AggregateConfig> aggregateConfigs = aggregateRegistry.getAggregateConfigs();

        ImmutableMap.Builder<Class<? extends Command>, CommandHandler> commandHandlerMapBuilder = ImmutableMap.builder();
        for(AggregateConfig aggregateConfig: aggregateConfigs){

            List<CommandHandler> aggregateCommandHandlers = aggregateConfig.getCommandHandlers();

            for(CommandHandler commandHandler: aggregateCommandHandlers){
                Class<? extends Command> commandClass = commandHandler.getCommandClass();

                commandHandlerMapBuilder.put(commandClass, commandHandler);
            }
        }

        Map<Class<? extends Command>, CommandHandler> commandHandlerMap = commandHandlerMapBuilder.build();
        setCommandHandlerMap(commandHandlerMap);
    }
}
