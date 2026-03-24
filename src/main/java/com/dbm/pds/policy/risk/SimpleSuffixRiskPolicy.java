package com.dbm.pds.policy.risk;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public class SimpleSuffixRiskPolicy implements RiskPolicy<String> {

    @Override
    public void evaluate(String candidate, PolicyContext context, PolicyEvaluation evaluation) {
        if (candidate != null && candidate.endsWith("!!!")) {
            evaluation.addScoreAdjustment("Risk", getName(), -2.0,
                    "Penalize high-volatility suffix");
        }
    }

    @Override
    public String getName() {
        return "SimpleSuffixRiskPolicy";
    }
}