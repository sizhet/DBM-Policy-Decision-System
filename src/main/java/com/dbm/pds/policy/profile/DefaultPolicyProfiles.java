package com.dbm.pds.policy.profile;

import com.dbm.pds.learning.PolicyWeights;

import java.util.HashMap;
import java.util.Map;

public final class DefaultPolicyProfiles {

    private DefaultPolicyProfiles() {
    }

    public static PolicyProfile safeV1() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 0.9);
        weights.setWeight("Constraint", 1.5);
        weights.setWeight("Risk", 1.4);
        weights.setWeight("Strategy", 0.9);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("goal", "prefer_short");
        metadata.put("description", "Conservative profile prioritizing constraint and risk control");

        return new PolicyProfile("safe", "v1", PolicyMode.SAFE, weights, metadata);
    }

    public static PolicyProfile aggressiveV1() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.3);
        weights.setWeight("Constraint", 1.0);
        weights.setWeight("Risk", 0.7);
        weights.setWeight("Strategy", 1.2);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("goal", "prefer_short");
        metadata.put("description", "Aggressive profile prioritizing achievement and action bias");

        return new PolicyProfile("aggressive", "v1", PolicyMode.AGGRESSIVE, weights, metadata);
    }

    public static PolicyProfile testV1() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Constraint", 1.0);
        weights.setWeight("Risk", 1.0);
        weights.setWeight("Strategy", 1.0);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("goal", "prefer_short");
        metadata.put("description", "Neutral profile for testing and baseline comparisons");

        return new PolicyProfile("test", "v1", PolicyMode.TEST, weights, metadata);
    }
}