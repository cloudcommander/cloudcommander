package com.cloudcommander.vendor.cqrs.commands.bus;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateConfig;
import com.cloudcommander.vendor.cqrs.aggregates.AggregateState;
import com.cloudcommander.vendor.cqrs.aggregates.registry.AggregateRegistry;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DefaultCommandBusDispatcherUnitTest {

    @Test
    public void testNoAggregates(){
        //Prepare
        DefaultCommandBusDispatcher commandBusDispatcher = new DefaultCommandBusDispatcher();
        MockAggregateRegistry aggregateRegistry = new MockAggregateRegistry(Collections.emptyList());
        commandBusDispatcher.setAggregateRegistry(aggregateRegistry);

        //Test
        Map<Class<? extends Command>, CommandHandler> commandHandlerMap = commandBusDispatcher.getCommandHandlerMap();

        //Verify
        assertTrue(commandHandlerMap.isEmpty());
    }

    @Test
    public void testMultipleAggregates(){
        //Prepare
        Class<? extends Command> aggregate1CmdHandler1CmdClass = Command1.class;
        CommandHandler aggregate1CmdHandler1 = mock(CommandHandler.class);
        when(aggregate1CmdHandler1.getCommandClass()).thenReturn(aggregate1CmdHandler1CmdClass);

        Class<? extends Command> aggregate1CmdHandler2CmdClass = Command2.class;
        CommandHandler aggregate1CmdHandler2 = mock(CommandHandler.class);
        when(aggregate1CmdHandler2.getCommandClass()).thenReturn(aggregate1CmdHandler2CmdClass);

        List<CommandHandler> aggregate1CommandHandlers = new ArrayList<>();
        aggregate1CommandHandlers.add(aggregate1CmdHandler1);
        aggregate1CommandHandlers.add(aggregate1CmdHandler2);

        AggregateConfig aggregateConfig1 = new AggregateConfig(AggregateState.class, aggregate1CommandHandlers, Collections.emptyList());

        AggregateConfig aggregateConfig2 = new AggregateConfig(AggregateState.class, Collections.emptyList(), Collections.emptyList());

        Class<? extends Command> aggregate3CmdHandler1CmdClass = Command3.class;
        CommandHandler aggregate3CmdHandler1 = mock(CommandHandler.class);
        when(aggregate3CmdHandler1.getCommandClass()).thenReturn(aggregate3CmdHandler1CmdClass);
        AggregateConfig aggregateConfig3 = new AggregateConfig(AggregateState.class, Collections.singletonList(aggregate3CmdHandler1), Collections.emptyList());

        List<AggregateConfig> aggregateConfigs = Arrays.asList(aggregateConfig1, aggregateConfig2, aggregateConfig3);
        MockAggregateRegistry aggregateRegistry = new MockAggregateRegistry(aggregateConfigs);

        DefaultCommandBusDispatcher commandBusDispatcher = new DefaultCommandBusDispatcher();
        commandBusDispatcher.setAggregateRegistry(aggregateRegistry);

        //Test
        Map<Class<? extends Command>, CommandHandler> commandHandlerMap = commandBusDispatcher.getCommandHandlerMap();

        //Verify
        assertEquals(3, commandHandlerMap.size());

        assertEquals(aggregate1CmdHandler1, commandHandlerMap.get(aggregate1CmdHandler1CmdClass));
        assertEquals(aggregate1CmdHandler2, commandHandlerMap.get(aggregate1CmdHandler2CmdClass));
        assertEquals(aggregate3CmdHandler1, commandHandlerMap.get(aggregate3CmdHandler1CmdClass));
    }

    //TODO tello test dispatch methods

    //TODO tello deadletter interceptor

    private class MockAggregateRegistry implements AggregateRegistry{

        private List<AggregateConfig> aggregateConfigs;

        private MockAggregateRegistry(List<AggregateConfig> aggregateConfigs){
            this.aggregateConfigs = aggregateConfigs;
        }

        @Override
        public List<AggregateConfig> getAggregateConfigs() {
            return aggregateConfigs;
        }
    }

    private class Command1 extends Command{

    }

    private class Command2 extends Command{

    }

    private class Command3 extends Command{

    }
}
