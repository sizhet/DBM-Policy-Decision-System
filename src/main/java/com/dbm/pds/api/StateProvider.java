package com.dbm.pds.api;

public interface StateProvider<X, S> {

    S buildState(X input, RuntimeContext ctx);

}