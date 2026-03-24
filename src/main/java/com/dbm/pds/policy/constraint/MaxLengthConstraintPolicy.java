package com.dbm.pds.policy.constraint;

import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluation;

public class MaxLengthConstraintPolicy implements ConstraintPolicy<String> {

    private final int maxLength;

    public MaxLengthConstraintPolicy(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void evaluate(String candidate, PolicyContext context, PolicyEvaluation evaluation) {
        if (candidate != null && candidate.length() > maxLength) {
            evaluation.reject("Constraint", getName(),
                    "Candidate length exceeds maxLength=" + maxLength);
        }
    }

    @Override
    public String getName() {
        return "MaxLengthConstraintPolicy";
    }
}