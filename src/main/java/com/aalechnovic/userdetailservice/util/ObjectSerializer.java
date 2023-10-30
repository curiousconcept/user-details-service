package com.aalechnovic.userdetailservice.util;

import java.io.Serializable;

/**
 * Arbitrary implementation of serializer is not allowed, there are many Remote Code Invocation vulnerabilities,
 * hence the implementation take into account such attack vectors.
 */
public interface ObjectSerializer {
    byte[] serialize(Serializable o);

    <T> T deserialize(byte[] bytesObj, Class<T> clazz);
}
