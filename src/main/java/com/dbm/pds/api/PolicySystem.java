package com.dbm.pds.api;

import java.util.List;

public interface PolicySystem<Y> {

    List<Y> applyPolicy(List<Y> candidates, RuntimeContext ctx);

}