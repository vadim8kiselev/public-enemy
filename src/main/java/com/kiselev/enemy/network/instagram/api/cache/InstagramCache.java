package com.kiselev.enemy.network.instagram.api.cache;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Supplier;

public class InstagramCache<Key, Value> {

    private Map<Key, Value> cache;

    public InstagramCache() {
        this.cache = Maps.newHashMap();
    }

    public Value cache(Key key, Supplier<Value> supplier) {
        if (isMemorized(key)) {
            return recall(key);
        } else {
            Value value = supplier.get();
            remember(key, value);
            return value;
        }
    }

    public boolean isMemorized(Key key) {
        if (key == null) {
            throw new RuntimeException("Key should be non null value");
        }
        return cache.containsKey(key);
    }

    public Value remember(Key key, Value value) {
        if (key == null || value == null) {
            throw new RuntimeException("Key and value should be non null values");
        }
        return cache.put(key, value);
    }

    public Value recall(Key key) {
        if (key == null) {
            throw new RuntimeException("Key should be non null value");
        }
        return cache.get(key);
    }

    public void forget() {
        cache.clear();
    }
}
