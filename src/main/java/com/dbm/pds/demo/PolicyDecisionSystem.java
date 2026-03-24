package com.dbm.pds.demo;

interface PolicyDecisionSystem {
    Y decide(X input, RuntimeContext ctx);
}