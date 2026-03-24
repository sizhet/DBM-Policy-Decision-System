package com.dbm.pds.api;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private final Map<String, Object> runtime = new HashMap<>();

    public void put(String key, Object value) {
        runtime.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) runtime.get(key);
    }

}