package com.kafka.serialize;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * 反序列化obj
 */
public class ObjectDeserializer implements Deserializer<Object> {
    public void configure(Map<String, ?> map, boolean b) {

    }

    public Object deserialize(String s, byte[] bytes) {
        return SerializeUtils.deserialize(bytes);
    }

    public void close() {

    }
}
