package com.cloudcommander.vendor.ddd.entities.akka.serialization;

import akka.serialization.SerializerWithStringManifest;
import com.cloudcommander.vendor.ddd.entities.Uuid;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands.DefaultProtoIncrementCommandConverter;
import com.cloudcommander.vendor.ddd.entities.akka.actors.counter.serialization.converters.protobuf.commands.dto.CounterActorCommands;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.Converter;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf.DefaultProtoUuidConverter;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf.ProtoUuidConverter;
import com.google.protobuf.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.NotSerializableException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Adrian Tello on 04/11/2017.
 */
@RunWith(JUnit4.class)
public class DddProtobufSerializerUnitTest {

    private final SerializerWithStringManifest serializer;

    private final IncrementCommand incrementCommand;

    private final Uuid.UUID protoUuid;

    private final CounterActorCommands.IncrementCommand incrementCommandProto;

    public DddProtobufSerializerUnitTest() throws NoSuchMethodException {
        final ProtoUuidConverter protoUuidConverter = new DefaultProtoUuidConverter();
        final DefaultProtoIncrementCommandConverter protoIncrementCommandConverter = new DefaultProtoIncrementCommandConverter();
        protoIncrementCommandConverter.setProtoUuidConverter(protoUuidConverter);

        final List<? extends Converter<?, ? extends Message>> protobufConverters = Collections.singletonList(protoIncrementCommandConverter);

        serializer = new DddProtobufSerializer(protobufConverters);

        //Test fixtures
        final UUID uuid = UUID.randomUUID();
        incrementCommand = new IncrementCommand(uuid);

        final Uuid.UUID.Builder protoUuidBuilder = Uuid.UUID.newBuilder();
        protoUuidBuilder.setLeastSigBits(uuid.getLeastSignificantBits());
        protoUuidBuilder.setMostSigBits(uuid.getMostSignificantBits());

        protoUuid = protoUuidBuilder.build();

        final CounterActorCommands.IncrementCommand.Builder incrementCommandProtoBuilder = CounterActorCommands.IncrementCommand.newBuilder();
        incrementCommandProtoBuilder.setAggregateId(protoUuid);
        incrementCommandProto = incrementCommandProtoBuilder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBinaryManifestNotFound(){
        serializer.toBinary("Non existent manifest for this object");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromBinaryManifestNotFound() throws NotSerializableException {
        //Prepare
        final byte[] protoUuidByteArray = protoUuid.toByteArray();

        //Test
        serializer.fromBinary(protoUuidByteArray, "dddproto-invalidmanifest");
    }

    @Test
    public void testToBinary(){
        //Test
        final byte[] bytes = serializer.toBinary(incrementCommand);

        //Verify
        Assert.assertArrayEquals(incrementCommandProto.toByteArray(), bytes);
    }

    @Test
    public void testFromBinary() throws NotSerializableException {
        //Prepare
        final String manifest = "dddproto-" + incrementCommand.getClass().getCanonicalName();

        //Test
        Object serializedObj = serializer.fromBinary(incrementCommandProto.toByteArray(), manifest);

        //Verify
        Assert.assertEquals(incrementCommand, serializedObj);
    }
}
