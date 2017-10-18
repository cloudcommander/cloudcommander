package com.cloudcommander.vendor.ddd.aggregates.akka.actors;

import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import akka.persistence.journal.Tagged;
import com.cloudcommander.vendor.ddd.aggregates.AggregateDefinition;
import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.queries.Query;
import com.cloudcommander.vendor.ddd.aggregates.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.aggregates.responses.UnhandledCommandResponse;
import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

public class AggregateActor<T extends Command, U extends Event, V extends Query, W extends Result, S extends State> extends AbstractPersistentActor {

    private static Logger LOG = LogManager.getLogger(AggregateActor.class);

    private final AggregateDefinition<T, U, V, W, S> aggregateDefinition;

    private S state;

    private Set<String> tags;

    private Receive receiveRecover;

    public AggregateActor(final AggregateDefinition<T, U, V, W, S> aggregateDefinition){
        this.aggregateDefinition = aggregateDefinition;

        setInitialState();
        createAggregateTags(aggregateDefinition);

        setupReceiveRecover();
    }

    private void setupReceiveRecover() {
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Restore from snapshots
        receiveBuilder.match(SnapshotOffer.class, snapshotOffer -> {
            state = (S)snapshotOffer.snapshot();
        });

        //Handle defined events
        List<? extends EventHandler<U, S>> eventHandlers = aggregateDefinition.getEventHandlers();
        for(EventHandler<U, S> eventHandler: eventHandlers){
            Class<U> eventClass = eventHandler.getEventClass();
            receiveBuilder.match(eventClass, event -> {
                state = eventHandler.handle(event, state);
            });
        }

        receiveRecover = receiveBuilder.build();
    }

    private void createAggregateTags(final AggregateDefinition<T, U, V, W, S> aggregateDefinition) {
        tags = new HashSet<>();

        String boundedContextName = getAggregateDefinition().getBoundedContextDefinition().getName();
        String aggregateTag = boundedContextName + "-" + aggregateDefinition.getName();
        tags.add(boundedContextName);
        tags.add(aggregateTag);
    }

    protected void setInitialState(){
        final StateFactory<S> stateFactory =  getAggregateDefinition().getStateFactory();
        state = stateFactory.create();
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveRecover;
    }

    @Override
    public Receive createReceive() {
        //Create Receive
        ReceiveBuilder receiveBuilder = ReceiveBuilder.create();

        //Handle defined commands
        List<? extends CommandHandler<T, U, S>> commandHandlers = getAggregateDefinition().getCommandHandlers();
        for(CommandHandler<T, U, S> commandHandler: commandHandlers){
            Class<T> commandClass = commandHandler.getCommandClass();
            receiveBuilder.match(commandClass, command -> {
                final U event = commandHandler.handle(command, state);
                Tagged taggedEvent = new Tagged(event, tags);

                persist(taggedEvent, param -> {
                   receiveRecover.onMessage().apply(event);

                    getSender().tell(event, getSelf());
                });
            });
        }

        //Handle defined queries
        List<? extends QueryHandler<V, W, S>> queryHandlers = getAggregateDefinition().getQueryHandlers();
        for(QueryHandler<V, W, S> queryHandler: queryHandlers){
            Class<V> queryClass = queryHandler.getQueryClass();
            receiveBuilder.match(queryClass, query -> {
                final Result result = queryHandler.handle(query, state);

                getSender().tell(result, getSelf());
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
        AggregateDefinition aggregateDefinition = getAggregateDefinition();
        String aggregateName = aggregateDefinition.getName();
        BoundedContextDefinition boundedContextDefinition = aggregateDefinition.getBoundedContextDefinition();

        return boundedContextDefinition.getName() + '-' + aggregateName + '-' + getSelf().path().name();
    }

    protected AggregateDefinition<T, U, V, W, S> getAggregateDefinition() {
        return aggregateDefinition;
    }

    public static <T extends Command, U extends Event, V extends Query, W extends Result, S extends com.cloudcommander.vendor.ddd.aggregates.states.State> Props props(final AggregateDefinition<T, U, V, W, S> aggregateDefinition) {
        return Props.create(AggregateActor.class, () -> new AggregateActor<>(aggregateDefinition));
    }
}
