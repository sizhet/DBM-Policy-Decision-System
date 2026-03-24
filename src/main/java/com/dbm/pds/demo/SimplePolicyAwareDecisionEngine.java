package com.dbm.pds.demo;

import com.dbm.pds.api.DecisionEngine;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.PolicyCandidate;

import java.util.List;

public class SimplePolicyAwareDecisionEngine implements DecisionEngine<String> {

    @Override
    public String select(List<PolicyCandidate<String>> candidates, RuntimeContext ctx) {
        String best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PolicyCandidate<String> c : candidates) {
            if (!c.isAllowed()) {
                continue;
            }

            double baseScore = 0.0;
            double finalScore = baseScore + c.getPolicyScoreAdjustment();

            if (best == null || finalScore > bestScore) {
                best = c.getCandidate();
                bestScore = finalScore;
            }
        }

        return best;
    }
}