package com.dbm.pds.policy.constraint;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public interface ConstraintPolicy<Y> {

    void evaluate(Y candidate, PolicyContext context, PolicyEvaluation evaluation);

    String getName();
}