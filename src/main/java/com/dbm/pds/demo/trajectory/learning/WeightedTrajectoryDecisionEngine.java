package com.dbm.pds.demo.trajectory.learning;

import com.dbm.pds.api.DecisionEngine;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.policy.PolicyCandidate;

import java.util.List;

public class WeightedTrajectoryDecisionEngine implements DecisionEngine<Trajectory> {

    @Override
    public Trajectory select(List<PolicyCandidate<Trajectory>> candidates, RuntimeContext ctx) {
        Trajectory best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PolicyCandidate<Trajectory> c : candidates) {
            if (!c.isAllowed()) {
                continue;
            }

            double score = c.getPolicyScoreAdjustment();

            if (best == null || score > bestScore) {
                best = c.getCandidate();
                bestScore = score;
            }
        }

        return best;
    }
}