package com.dbm.pds.policy.goal;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public interface GoalPolicy<Y> {

    void evaluate(Y candidate, PolicyContext context, PolicyEvaluation evaluation);

    String getName();
}