package com.dbm.pds.api;

public interface KnowledgeModel<X, Y> {

    Y infer(X input, RuntimeContext ctx);

}