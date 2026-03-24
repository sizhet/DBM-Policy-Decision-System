package com.dbm.pds.demo;

import com.dbm.pds.policy.PolicyCandidate;

import java.util.List;

public final class SimplePolicyCandidateSelector {

    private SimplePolicyCandidateSelector() {
    }

    public static String chooseBest(List<PolicyCandidate<String>> candidates) {
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