package com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf;

import java.util.UUID;
import com.cloudcommander.vendor.ddd.entities.Uuid;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public class DefaultProtoUuidConverter implements ProtoUuidConverter {
    @Override
    public UUID toDomain(Uuid.UUID dto) {
        final long mostSigBits = dto.getMostSigBits();
        final long leastSigBits = dto.getLeastSigBits();

        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public Uuid.UUID toDto(UUID uuid) {
        final Uuid.UUID.Builder dtoBuilder = Uuid.UUID.newBuilder();

        dtoBuilder.setMostSigBits(uuid.getMostSignificantBits());
        dtoBuilder.setLeastSigBits(uuid.getLeastSignificantBits());

        return dtoBuilder.build();
    }
}
