package com.dbm.pds.policy;

import com.dbm.pds.api.RuntimeContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PolicyContext {

    private final RuntimeContext runtimeContext;
    private final Object state;
    private final Map<String, Object> attributes = new HashMap<>();

    public PolicyContext(RuntimeContext runtimeContext, Object state) {
        this.runtimeContext = runtimeContext;
        this.state = state;
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    public Object getState() {
        return state;
    }

    public void putAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    public Map<String, Object> getAttributesView() {
        return Collections.unmodifiableMap(attributes);
    }
}