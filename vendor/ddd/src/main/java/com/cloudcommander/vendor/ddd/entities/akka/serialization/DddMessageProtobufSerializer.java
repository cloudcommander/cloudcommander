package com.cloudcommander.vendor.ddd.entities.akka.serialization;

import akka.serialization.SerializerWithStringManifest;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.Converter;
import com.google.protobuf.Message;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public class DddMessageProtobufSerializer extends SerializerWithStringManifest {

    private final Map<String, Converter<byte[], Message>> convertersByManifestMap;

    public DddMessageProtobufSerializer(final  List<? extends Converter<byte[], Message>> byteArrayProtobufConverters){
        convertersByManifestMap = new HashMap<>(byteArrayProtobufConverters.size());

        for(Converter<byte[], Message> byteArrayProtobufConverter: byteArrayProtobufConverters){
            final Class<Message> messageClass = byteArrayProtobufConverter.getDtoClass();
            final String manifest = manifestByClass(messageClass);

            convertersByManifestMap.put(manifest, byteArrayProtobufConverter);
        }
    }

    @Override
    public int identifier() {
        return 1028228201;
    }

    @Override
    public String manifest(Object o) {
        return manifestByClass(o.getClass());
    }

    protected String manifestByClass(Class<? extends Object> objectClass) {
        return "dddproto-" + objectClass.getCanonicalName(); //TODO tello use a compact manifest
    }

    @Override
    public byte[] toBinary(Object o) {
        if(o instanceof Message){
            final String manifest = manifest(o);
            final Converter<byte[], Message> converter = getConverterByManifest(manifest);

            final Message message = (Message)o;
            return converter.toDomain(message);
        }else{
            throw new IllegalArgumentException("The object to serialize isn't an instance of a protobuf message.");
        }
    }

    protected Converter<byte[], Message> getConverterByManifest(final String manifiest){
        final Converter<byte[], Message> converter = convertersByManifestMap.get(manifiest);

        if(converter == null){
            throw new IllegalArgumentException("Converter for manifest \"" + manifiest + "\" not found.");
        }else{
            return converter;
        }
    }

    @Override
    public Object fromBinary(byte[] bytes, String manifiest) throws NotSerializableException {
        final Converter<byte[], Message> converter = getConverterByManifest(manifiest);
        return converter.toDto(bytes);
    }
}
