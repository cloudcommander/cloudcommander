package com.cloudcommander.vendor.ddd.entities.akka.actors.serialization;

import akka.serialization.SerializerWithStringManifest;

import java.io.NotSerializableException;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public class DddMessageProtobufSerializer extends SerializerWithStringManifest {
    @Override
    public int identifier() {
        return 1028228201;
    }

    @Override
    public String manifest(Object o) {
        return "dddproto";
    }

    @Override
    public byte[] toBinary(Object o) {
        return new byte[0]; //TODO tello
    }

    @Override
    public Object fromBinary(byte[] bytes, String manifiest) throws NotSerializableException {
        return null;  //TODO tello
    }
}
