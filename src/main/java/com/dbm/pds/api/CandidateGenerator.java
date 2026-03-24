package com.dbm.pds.api;

import java.util.List;

public interface CandidateGenerator<S, Y> {

    List<Y> generate(S state, RuntimeContext ctx);

}