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
    public void testToSerializable(){
        UUID originalUuid = UUID.randomUUID();

        final Uuid.UUID convertedUuid = protoUuidConverter.toSerializable(originalUuid);

        Assert.assertEquals(originalUuid.getLeastSignificantBits(), convertedUuid.getLeastSigBits());
        Assert.assertEquals(originalUuid.getMostSignificantBits(), convertedUuid.getMostSigBits());
    }

    @Test
    public void testFromSerializable(){
        UUID templateUuid = UUID.randomUUID();

        final Uuid.UUID.Builder sourceUuidBuilder = Uuid.UUID.newBuilder();
        sourceUuidBuilder.setMostSigBits(templateUuid.getMostSignificantBits());
        sourceUuidBuilder.setLeastSigBits(templateUuid.getLeastSignificantBits());
        final Uuid.UUID sourceUuid = sourceUuidBuilder.build();

        UUID convertedUuid = protoUuidConverter.fromSerializable(sourceUuid);

        Assert.assertEquals(templateUuid, convertedUuid);
    }
}
