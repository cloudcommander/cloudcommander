package com.cloudcommander.vendor.ddd.entities.serialization.converters;

/**
 * Created by Adrian Tello on 01/11/2017.
 */
public interface Converter<S, O> {
    S fromSerializable(O object);

    O toSerializable(S object);
}
