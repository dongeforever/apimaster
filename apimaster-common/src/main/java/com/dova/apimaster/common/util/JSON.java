package com.dova.apimaster.common.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class JSON {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T read(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json.getBytes(Charset.defaultCharset()), type);
    }

    public static <T> T safeRead(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json.getBytes(Charset.defaultCharset()), type);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }


    public static String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static String uncheckedToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> List<T> safeReadList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static ObjectNode newObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static JsonNode of(String text)throws IOException {
        return objectMapper.readTree(text);
    }

    public static ObjectNode unCheckedOf(String text){
        try{
            return (ObjectNode)objectMapper.readTree(text);
        }catch (Exception e){
            throw Throwables.propagate(e);
        }
    }
}
