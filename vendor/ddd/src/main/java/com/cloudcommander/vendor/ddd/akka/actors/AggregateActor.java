package com.cloudcommander.vendor.ddd.akka.actors;

import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateActor extends AbstractPersistentActor {

    private static Logger LOG = LogManager.getLogger(AggregateActor.class);

    private final AggregateDefinition aggregateDefinition;

    private AggregateState state;

    public AggregateActor(final AggregateDefinition aggregateDefinition){
        this.aggregateDefinition = aggregateDefinition;
    }

    @Override
    public Receive createReceiveRecover() {
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Restore from snapshots
        receiveBuilder.match(SnapshotOffer.class, snapshotOffer -> {
            state = (AggregateState)snapshotOffer.snapshot();
        });

        //Handle defined events
        List<EventHandler<Event, AggregateState>> eventHandlers = aggregateDefinition.getEventHandlers();
        for(EventHandler<Event, AggregateState> eventHandler: eventHandlers){
            Class<Event> eventClass = eventHandler.getEventClass();
            receiveBuilder.match(eventClass, event -> {
                eventHandler.handle(event, state);
            });
        }

        return receiveBuilder.build();
    }

    @Override
    public Receive createReceive() {
        //Create event handler map
        List<EventHandler<Event, AggregateState>> eventHandlers = aggregateDefinition.getEventHandlers();

        final Map<Class<Event>, EventHandler<Event, AggregateState>> eventHandlerMap = new HashMap<>(eventHandlers.size());
        for(final EventHandler<Event, AggregateState> eventHandler: eventHandlers){
            Class<Event> eventClass = eventHandler.getEventClass();

            eventHandlerMap.put(eventClass, eventHandler);
        }

        //Create Receive
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Handle defined commands
        List<CommandHandler<Command, AggregateState>> commandHandlers = aggregateDefinition.getCommandHandlers();
        for(CommandHandler<Command, AggregateState> commandHandler: commandHandlers){
            Class<Command> commandClass = commandHandler.getCommandClass();
            receiveBuilder.match(commandClass, command -> {
                final Event event = commandHandler.handle(command, state);

                persist(event, param -> {
                    Class<? extends Event> eventClass = event.getClass();
                    EventHandler<Event, AggregateState> eventHandler = eventHandlerMap.get(eventClass);

                    if(eventHandler != null){
                        eventHandler.handle(event, state);
                    }
                });
            });
        }

        receiveBuilder.matchAny(o -> {
            if(LOG.isWarnEnabled()){
                LOG.warn("Unhandled command type [" + o.getClass() + "] for actor: [" + getSelf().path().toStringWithoutAddress() + "]");
            }

            UnhandledCommandResponse unhandledCommandResponse = new UnhandledCommandResponse(o.getClass());
            getSender().tell(unhandledCommandResponse, getSelf());
        });

        return receiveBuilder.build();
    }

    @Override
    public String persistenceId() {
        String aggregateName = aggregateDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = aggregateDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + aggregateName + '-' + getSelf().path().name();
    }

    protected AggregateDefinition getAggregateDefinition() {
        return aggregateDefinition;
    }

    public static Props props(final AggregateDefinition aggregateDefinition) {
        return Props.create(AggregateActor.class, () -> new AggregateActor(aggregateDefinition));
    }
}
