package com.dbm.pds.policy.profile;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.learning.PolicyWeights;

public final class PolicyProfileApplier {

    private PolicyProfileApplier() {
    }

    public static void applyToRuntime(PolicyProfile profile, RuntimeContext ctx) {
        ctx.put("policy.profile.name", profile.getProfileName());
        ctx.put("policy.profile.version", profile.getVersion());
        ctx.put("policy.profile.mode", profile.getMode().name());
        ctx.put("policy.profile.metadata", profile.getMetadata());
        ctx.put("policy.goal", profile.getMetadata().get("goal"));
    }

    public static PolicyWeights copyWeights(PolicyProfile profile) {
        PolicyWeights copy = new PolicyWeights();
        for (java.util.Map.Entry<String, Double> e : profile.getWeights().asMap().entrySet()) {
            copy.setWeight(e.getKey(), e.getValue());
        }
        return copy;
    }
}