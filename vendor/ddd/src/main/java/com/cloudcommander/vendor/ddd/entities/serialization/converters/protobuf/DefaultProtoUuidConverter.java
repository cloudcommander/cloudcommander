package com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf;

import java.util.UUID;
import com.cloudcommander.vendor.ddd.entities.Uuid;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public class DefaultProtoUuidConverter implements ProtoUuidConverter {
    @Override
    public UUID fromSerializable(Uuid.UUID uuid) {
        final long mostSigBits = uuid.getMostSigBits();
        final long leastSigBits = uuid.getLeastSigBits();

        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public Uuid.UUID toSerializable(UUID uuid) {
        final Uuid.UUID.Builder newUuidBuilder = Uuid.UUID.newBuilder();

        newUuidBuilder.setMostSigBits(uuid.getMostSignificantBits());
        newUuidBuilder.setLeastSigBits(uuid.getLeastSignificantBits());

        return newUuidBuilder.build();
    }
}
