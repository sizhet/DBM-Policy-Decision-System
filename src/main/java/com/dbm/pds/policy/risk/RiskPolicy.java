package com.dbm.pds.policy.risk;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public interface RiskPolicy<Y> {

    void evaluate(Y candidate, PolicyContext context, PolicyEvaluation evaluation);

    String getName();
}