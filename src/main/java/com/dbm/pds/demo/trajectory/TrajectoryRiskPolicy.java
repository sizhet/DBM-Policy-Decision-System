package com.dbm.pds.demo.trajectory;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;
import com.dbm.pds.policy.risk.RiskPolicy;

public class TrajectoryRiskPolicy implements RiskPolicy<Trajectory> {

    @Override
    public void evaluate(Trajectory t, PolicyContext ctx, PolicyEvaluation eval) {
        double penalty = -3.0 * t.getRisk();
        eval.addScoreAdjustment("Risk", getName(), penalty, "Risk penalty");
    }

    @Override
    public String getName() {
        return "TrajectoryRiskPolicy";
    }
}