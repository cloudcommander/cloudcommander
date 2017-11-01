package com.cloudcommander.vendor.ddd.entities.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.cloudcommander.vendor.ddd.entities.EntityDefinition;
import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.states.State;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EntityRouter <U, S extends State, F extends FSMState> extends AbstractActor {

    private HashMap<Object, ActorRef>  entityRefs = new LinkedHashMap<>();

    private EntityDefinition<U, S, F> entityDefinition;

    public EntityRouter(EntityDefinition<U, S, F> entityDefinition){
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

    public static <U, S extends State, F extends FSMState> Props props(final EntityDefinition<U, S, F> entityDefinition) {
        return Props.create(EntityRouter.class, () -> new EntityRouter<>(entityDefinition));
    }
}
