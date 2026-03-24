package com.dbm.pds.api;

import java.util.List;
import java.util.Map;

public interface PolicyDecisionSystem<X, Y> {

    DecisionResult<Y> decide(X input, RuntimeContext ctx);

}