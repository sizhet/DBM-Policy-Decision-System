package com.dbm.pds.policy.goal;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public class PreferShortGoalPolicy implements GoalPolicy<String> {

    @Override
    public void evaluate(String candidate, PolicyContext context, PolicyEvaluation evaluation) {
        Object goal = context.getRuntimeContext().get("goal");
        if ("prefer_short".equals(goal)) {
            double delta = -0.1 * candidate.length();
            evaluation.addScoreAdjustment("Goal", getName(), delta,
                    "Prefer shorter candidates");
        }
    }

    @Override
    public String getName() {
        return "PreferShortGoalPolicy";
    }
}