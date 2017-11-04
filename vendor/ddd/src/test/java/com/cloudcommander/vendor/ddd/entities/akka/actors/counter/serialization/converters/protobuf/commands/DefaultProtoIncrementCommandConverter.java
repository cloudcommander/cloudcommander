package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands;

import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands.dto.CounterActorCommands;
import com.cloudcommander.vendor.ddd.entities.Uuid;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf.ProtoUuidConverter;
import org.springframework.beans.factory.annotation.Required;

import java.util.UUID;

/**
 * Created by Adrian Tello on 03/11/2017.
 */
public class DefaultProtoIncrementCommandConverter implements ProtoIncrementCommandConverter{

    private ProtoUuidConverter protoUuidConverter;

    @Override
    public IncrementCommand toDomain(CounterActorCommands.IncrementCommand dto) {
        final UUID uuidDomain = protoUuidConverter.toDomain(dto.getAggregateId());

        return new IncrementCommand(uuidDomain);
    }

    @Override
    public CounterActorCommands.IncrementCommand toDto(IncrementCommand domainObj) {
        final Uuid.UUID aggregateIdDto = protoUuidConverter.toDto(domainObj.getAggregateId());

        final CounterActorCommands.IncrementCommand.Builder dtoBuilder = CounterActorCommands.IncrementCommand.newBuilder();
        dtoBuilder.setAggregateId(aggregateIdDto);

        return dtoBuilder.build();
    }

    @Override
    public Class<IncrementCommand> getDomainClass() {
        return IncrementCommand.class;
    }

    @Override
    public Class<CounterActorCommands.IncrementCommand> getDtoClass() {
        return CounterActorCommands.IncrementCommand.class;
    }

    protected ProtoUuidConverter getProtoUuidConverter() {
        return protoUuidConverter;
    }

    @Required
    public void setProtoUuidConverter(ProtoUuidConverter protoUuidConverter) {
        this.protoUuidConverter = protoUuidConverter;
    }
}
