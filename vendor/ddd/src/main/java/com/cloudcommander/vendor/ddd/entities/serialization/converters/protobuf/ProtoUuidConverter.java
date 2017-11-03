package com.cloudcommander.vendor.ddd.entities.serialization.converters.protobuf;

import com.cloudcommander.vendor.ddd.entities.serialization.converters.Converter;
import com.cloudcommander.vendor.ddd.entities.Uuid;

import java.util.UUID;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public interface ProtoUuidConverter extends Converter<UUID, Uuid.UUID>{
}
