package com.cloudcommander.vendor.cqrs.aggregates.registry;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateConfig;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventHandler;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultAggregateRegistry implements AggregateRegistry{
    private List<AggregateConfig> aggregateConfigs;

    public List<AggregateConfig> getAggregateConfigs() {
        return aggregateConfigs;
    }

    @Autowired
    @Required
    public void setAggregateConfigs(List<AggregateConfig> aggregateConfigs) {
        validateAggregateConfigs(aggregateConfigs);

        this.aggregateConfigs = ImmutableList.copyOf(aggregateConfigs);
    }

    private void validateAggregateConfigs(List<AggregateConfig> aggregateConfigs) {
        validateCommandClasses(aggregateConfigs);
        validateEventClasses(aggregateConfigs);
    }

    private void validateCommandClasses(List<AggregateConfig> aggregateConfigs){
        Set<Class<Command>> commandClassesSet = new HashSet<>();

        for(AggregateConfig aggregateConfig: aggregateConfigs){
            List<CommandHandler> commandHandlers = aggregateConfig.getCommandHandlers();
            for(CommandHandler commandHandler: commandHandlers){
                Class<Command> commandClass = commandHandler.getCommandClass();

                if(commandClassesSet.contains(commandClass)){
                    throw new IllegalArgumentException("The Command Class [" + commandClass.getCanonicalName() + "] is already registered.");
                }else{
                    commandClassesSet.add(commandClass);
                }
            }
        }
    }

    private void validateEventClasses(List<AggregateConfig> aggregateConfigs){
        Set<Class<Event>> eventClassesSet = new HashSet<>();

        for(AggregateConfig aggregateConfig: aggregateConfigs){
            List<EventHandler> eventHandlers = aggregateConfig.getEventHandlers();
            for(EventHandler eventHandler: eventHandlers){
                Class<Event> eventClass = eventHandler.getEventClass();

                if(eventClassesSet.contains(eventClass)){
                    throw new IllegalArgumentException("The Event Class [" + eventClass.getCanonicalName() + "] is already registered.");
                }else{
                    eventClassesSet.add(eventClass);
                }
            }
        }
    }
}
