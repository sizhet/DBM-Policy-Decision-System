package com.dbm.pds.policy;

public class DefaultPolicyAggregator implements PolicyAggregator {

    @Override
    public <Y> PolicyCandidate<Y> aggregate(Y candidate, PolicyEvaluation evaluation) {
        return new PolicyCandidate<>(
                candidate,
                evaluation.isAllowed(),
                evaluation.getScoreAdjustment(),
                evaluation.getNotes(),
                evaluation.getScoreBreakdown()
        );
    }
}