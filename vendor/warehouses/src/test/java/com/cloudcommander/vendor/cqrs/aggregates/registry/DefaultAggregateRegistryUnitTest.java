package com.cloudcommander.vendor.cqrs.aggregates.registry;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateConfig;
import com.cloudcommander.vendor.cqrs.aggregates.AggregateState;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DefaultAggregateRegistryUnitTest {

    @Test
    public void testEmptyConfig(){
        //Prepare
        DefaultAggregateRegistry aggregateRegistry = new DefaultAggregateRegistry();
        aggregateRegistry.setAggregateConfigs(Collections.emptyList());

        //Test
        List<AggregateConfig> aggregateConfigs = aggregateRegistry.getAggregateConfigs();

        //Verify
        assertEquals(0, aggregateConfigs.size());
    }

    @Test
    public void testValid(){
        //Prepare
        CommandHandler aggregate1CommandHandler1 = mock(CommandHandler.class);
        when(aggregate1CommandHandler1.getCommandClass()).thenReturn(Aggregate1Command1.class);

        CommandHandler aggregate1CommandHandler2 = mock(CommandHandler.class);
        when(aggregate1CommandHandler2.getCommandClass()).thenReturn(Aggregate1Command2.class);

        List<CommandHandler> aggregate1CommandHandlers = Arrays.asList(aggregate1CommandHandler1,aggregate1CommandHandler2);


        EventHandler aggregate1EventHandler1 = mock(EventHandler.class);
        when(aggregate1EventHandler1.getEventClass()).thenReturn(Aggregate1Event1.class);

        List<EventHandler> aggregate1EventHandlers = Collections.singletonList(aggregate1EventHandler1);

        AggregateConfig aggregateConfig1 = new AggregateConfig(Aggregate1State.class, aggregate1CommandHandlers, aggregate1EventHandlers);


        CommandHandler aggregate2CommandHandler = mock(CommandHandler.class);
        when(aggregate2CommandHandler.getCommandClass()).thenReturn(Aggregate2Command.class);

        List<CommandHandler> aggregate2CommandHandlers = Collections.singletonList(aggregate2CommandHandler);

        EventHandler aggregate2EventHandler = mock(EventHandler.class);
        when(aggregate2EventHandler.getEventClass()).thenReturn(Aggregate2Event.class);

        List<EventHandler> aggregate2EventHandlers = Collections.singletonList(aggregate2EventHandler);

        AggregateConfig aggregateConfig2 = new AggregateConfig(Aggregate2State.class, aggregate2CommandHandlers, aggregate2EventHandlers);

        DefaultAggregateRegistry aggregateRegistry = new DefaultAggregateRegistry();
        aggregateRegistry.setAggregateConfigs(Arrays.asList(aggregateConfig1, aggregateConfig2));

        //Test
        List<AggregateConfig> aggregateConfigs = aggregateRegistry.getAggregateConfigs();

        //Verify
        assertEquals(2, aggregateConfigs.size());
    }

    //TODO test repeated command classes

    //TODO tello test repeated event classes

    private class Aggregate1State extends AggregateState{

    }

    private class Aggregate1Command1 extends Command{

    }

    private class Aggregate1Command2 extends Command{

    }

    private class Aggregate1Event1 extends Event{

    }

    private class Aggregate2State extends AggregateState{

    }

    private class Aggregate2Command extends Command{

    }

    private class Aggregate2Event extends Command{

    }
}
