package com.aalechnovic.userdetailservice.util;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class ObjectSerializerImpl implements ObjectSerializer {

    public ObjectSerializerImpl() {
    }

    @Override
    public byte[] serialize(Serializable o){
        return SerializationUtils.serialize(o);
    }

    @Override
    public <T> T deserialize(byte[] bytesObj, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(bytesObj));
    }
}
