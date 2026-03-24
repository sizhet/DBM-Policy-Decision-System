package com.dbm.pds.demo;

import com.dbm.pds.api.*;

import java.util.ArrayList;
import java.util.List;

public class SimplePDS implements PolicyDecisionSystem<String, String> {

    @Override
    public DecisionResult<String> decide(String input, RuntimeContext ctx) {

        // II: state
        String state = input.toUpperCase();

        // III: candidates
        List<String> candidates = new ArrayList<>();
        candidates.add(state);
        candidates.add(state + "!!!");
        candidates.add(state + "?");

        // V: policy
        String goal = ctx.get("goal");
        if ("prefer_short".equals(goal)) {
            candidates.removeIf(c -> c.length() > 15);
        }

        // IV: decision
        String best = candidates.get(0);

        return new DecisionResult<>(best, candidates);
    }
}