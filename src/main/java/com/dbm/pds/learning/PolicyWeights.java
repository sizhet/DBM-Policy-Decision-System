package com.dbm.pds.learning;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PolicyWeights {

    private final Map<String, Double> weights = new HashMap<>();

    public void setWeight(String policyType, double weight) {
        weights.put(policyType, weight);
    }

    public double getWeight(String policyType) {
        Double value = weights.get(policyType);
        return value == null ? 1.0 : value;
    }

    public Map<String, Double> asMap() {
        return Collections.unmodifiableMap(weights);
    }
}