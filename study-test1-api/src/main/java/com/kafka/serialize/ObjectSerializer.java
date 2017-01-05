package com.kafka.serialize;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * 序列化obj
 */
public class ObjectSerializer implements Serializer<Object> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public byte[] serialize(String s, Object o) {
        return SerializeUtils.serialize(o);
    }

    public void close() {

    }
}
