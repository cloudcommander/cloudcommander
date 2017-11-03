package com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.cloudcommander.vendor.ddd.entities.Uuid;

import java.util.UUID;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
@RunWith(JUnit4.class)
public class DefaultProtoUuidConverterUnitTest {

    private final ProtoUuidConverter protoUuidConverter = new DefaultProtoUuidConverter();

    @Test
    public void testToDto(){
        UUID originalUuid = UUID.randomUUID();

        final Uuid.UUID dto = protoUuidConverter.toDto(originalUuid);

        Assert.assertEquals(originalUuid.getLeastSignificantBits(), dto.getLeastSigBits());
        Assert.assertEquals(originalUuid.getMostSignificantBits(), dto.getMostSigBits());
    }

    @Test
    public void testToDomain(){
        UUID templateUuid = UUID.randomUUID();

        final Uuid.UUID.Builder dtoBuilder = Uuid.UUID.newBuilder();
        dtoBuilder.setMostSigBits(templateUuid.getMostSignificantBits());
        dtoBuilder.setLeastSigBits(templateUuid.getLeastSignificantBits());
        final Uuid.UUID dtoUuid = dtoBuilder.build();

        final UUID convertedUuid = protoUuidConverter.toDomain(dtoUuid);

        Assert.assertEquals(templateUuid, convertedUuid);
    }
}
