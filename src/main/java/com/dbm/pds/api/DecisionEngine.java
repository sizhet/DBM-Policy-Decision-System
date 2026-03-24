package com.dbm.pds.api;

import java.util.List;

import com.dbm.pds.policy.PolicyCandidate;

public interface DecisionEngine<Y> {

    //Y select(List<Y> candidates, RuntimeContext ctx);

    Y select(List<PolicyCandidate<Y>> candidates, RuntimeContext ctx);
}