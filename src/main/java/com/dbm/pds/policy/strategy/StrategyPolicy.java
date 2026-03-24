package com.dbm.pds.policy.strategy;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public interface StrategyPolicy<Y> {

    void evaluate(Y candidate, PolicyContext context, PolicyEvaluation evaluation);

    String getName();
}