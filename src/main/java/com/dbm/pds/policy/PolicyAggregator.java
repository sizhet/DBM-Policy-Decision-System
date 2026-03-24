package com.dbm.pds.policy;

public interface PolicyAggregator {

    <Y> PolicyCandidate<Y> aggregate(Y candidate, PolicyEvaluation evaluation);
}