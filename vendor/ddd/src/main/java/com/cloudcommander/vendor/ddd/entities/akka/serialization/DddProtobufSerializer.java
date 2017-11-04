package com.cloudcommander.vendor.ddd.entities.akka.serialization;

import akka.serialization.SerializerWithStringManifest;
import com.cloudcommander.vendor.ddd.entities.serialization.converters.Converter;
import com.google.protobuf.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.NotSerializableException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public class DddProtobufSerializer extends SerializerWithStringManifest {

    private final Map<String, ConverterCache> converterCacheByManifestMap;

    public DddProtobufSerializer(final  List<? extends Converter<?, ? extends Message>> protobufConverters) throws NoSuchMethodException {
        converterCacheByManifestMap = new HashMap<>(protobufConverters.size());

        for(final Converter protobufConverter: protobufConverters) {
            final Class<? extends Message> messageClass = protobufConverter.getDtoClass();
            final Method parseFromMethod = messageClass.getMethod("parseFrom", byte[].class);

            final ConverterCache converterCache = new ConverterCache(parseFromMethod, protobufConverter);

            final Class<Object> domainClass = protobufConverter.getDomainClass();
            final String manifest = manifestByClass(domainClass);
            converterCacheByManifestMap.put(manifest, converterCache);
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
        final String manifest = manifestByClass(o.getClass());
        final ConverterCache converterCache = getConverterCacheByManifest(manifest);
        final Converter<Object, ? extends Message> messageConverter = converterCache.getMessageConverter();
        final Message message = messageConverter.toDto(o);
        return message.toByteArray();
    }

    protected ConverterCache getConverterCacheByManifest(final String manifiest){
        final ConverterCache converterCache = converterCacheByManifestMap.get(manifiest);

        if(converterCache == null){
            throw new IllegalArgumentException("Converter for manifest \"" + manifiest + "\" not found.");
        }else{
            return converterCache;
        }
    }

    @Override
    public Object fromBinary(byte[] bytes, String manifiest) throws NotSerializableException {
        final ConverterCache converterCache = getConverterCacheByManifest(manifiest);

        //Convert byte array to protobuf messages
        final Method parseFromMethod = converterCache.getParseFromMethod();

        final Message message;
        try {
            message = (Message)parseFromMethod.invoke(null, new Object[]{bytes});

        } catch (IllegalAccessException| InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
        
        return convertToDomain(message, converterCache.getMessageConverter());
    }

    private <M extends Message> Object convertToDomain(final Message message, final Converter<?, M> messageConverter) {
        return messageConverter.toDomain((M)message);
    }

    @EqualsAndHashCode
    @Value
    protected class ConverterCache{
        private Method parseFromMethod;

        private Converter<Object, ? extends Message> messageConverter;
    }
}
