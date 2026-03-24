package com.dbm.pds.policy.strategy;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public class PreferFirstStrategyPolicy implements StrategyPolicy<String> {

    @Override
    public void evaluate(String candidate, PolicyContext context, PolicyEvaluation evaluation) {
        Object state = context.getState();
        if (state instanceof String && candidate.equals(state)) {
            evaluation.addScoreAdjustment("Strategy", getName(), 1.0,
                    "Prefer direct state-preserving candidate");
        }
    }

    @Override
    public String getName() {
        return "PreferFirstStrategyPolicy";
    }
}