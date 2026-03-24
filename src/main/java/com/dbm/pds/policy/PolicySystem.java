package com.dbm.pds.policy;

import java.util.List;

public interface PolicySystem<Y> {

    List<PolicyCandidate<Y>> evaluate(List<Y> candidates, PolicyContext context);
}