package com.aalechnovic.userdetailservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectSerializer {

    private final ObjectMapper objectMapper;

    public ObjectSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] serialize(Object o){
        try {
            return this.objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deserialize(byte[] bytesObj, Class<T> clazz){
        try {
            return this.objectMapper.reader().readValue(bytesObj, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
