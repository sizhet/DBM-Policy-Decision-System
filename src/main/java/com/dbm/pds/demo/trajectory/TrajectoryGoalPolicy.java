package com.dbm.pds.demo.trajectory;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;
import com.dbm.pds.policy.goal.GoalPolicy;

public class TrajectoryGoalPolicy implements GoalPolicy<Trajectory> {

    @Override
    public void evaluate(Trajectory t, PolicyContext ctx, PolicyEvaluation eval) {
        double delta = -0.2 * t.getLength();
        eval.addScoreAdjustment("Goal", getName(), delta, "Prefer shorter route");
    }

    @Override
    public String getName() {
        return "TrajectoryGoalPolicy";
    }
}