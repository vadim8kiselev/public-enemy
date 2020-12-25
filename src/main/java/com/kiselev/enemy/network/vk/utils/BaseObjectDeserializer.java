package com.kiselev.enemy.network.vk.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class BaseObjectDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return json.getAsJsonObject().get("title").getAsString();
    }
}
