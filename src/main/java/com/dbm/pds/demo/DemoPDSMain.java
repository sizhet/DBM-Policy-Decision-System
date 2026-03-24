package com.dbm.pds.demo;

import com.dbm.pds.api.*;

import java.util.Arrays;
import java.util.List;

public class DemoPDSMain {

    public static void main(String[] args) {

        PolicyDecisionSystem<String, String> pds =
                new SimplePDS();

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        DecisionResult<String> result = pds.decide("hello world", ctx);

        System.out.println("Chosen: " + result.getChosen());
        System.out.println("Candidates: " + result.getCandidates());
    }
}