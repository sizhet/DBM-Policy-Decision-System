package com.dbm.pds.demo.trajectory.learning;

import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.learning.PolicyFeedback;

public class TrajectoryOutcomeEvaluator {

    public PolicyFeedback evaluate(Trajectory chosen) {
        if (chosen == null) {
            return new PolicyFeedback(false, -1.0);
        }

        // toy rule:
        // high risk route tends to be bad
        // low risk route tends to be good
        // balanced route is mildly good
        if (chosen.getRisk() >= 0.8) {
            return new PolicyFeedback(false, -1.0);
        }

        if (chosen.getRisk() <= 0.2) {
            return new PolicyFeedback(true, 1.0);
        }

        return new PolicyFeedback(true, 0.5);
    }
}