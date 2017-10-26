package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EntityRouter extends AbstractActor {

    private HashMap<Object, ActorRef>  entityRefs = new LinkedHashMap<>();

    private EntityDefinition<Object, Command<Object>, Event<Object>, Query<Object>, Result<Object>, State, FSMState> entityDefinition;

    public EntityRouter(EntityDefinition<Object, Command<Object>, Event<Object>, Query<Object>, Result<Object>, State, FSMState> entityDefinition){
        this.entityDefinition = entityDefinition;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Command.class, this::handleCommand)
                .build();
    }

    protected void handleCommand(Command command){
        Object targetId = command.getAggregateId();

        ActorRef entityRef = entityRefs.get(targetId);
        if(entityRef == null){
            entityRef = createEntity(targetId);
            entityRefs.put(targetId, entityRef);
        }

        entityRef.forward(command, getContext());
    }

    protected ActorRef createEntity(Object targetId){
        return getContext().actorOf(EntityActor.props(entityDefinition), targetId.toString());
    }

    public static Props props(final EntityDefinition<Object, Command<Object>, Event<Object>, Query<Object>, Result<Object>, State, FSMState> entityDefinition) {
        return Props.create(EntityRouter.class, () -> new EntityRouter(entityDefinition));
    }
}
