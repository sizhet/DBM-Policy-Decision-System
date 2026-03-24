package com.dbm.pds.api;

import java.util.List;

public interface DecisionEngine<Y> {

    Y select(List<Y> candidates, RuntimeContext ctx);

}