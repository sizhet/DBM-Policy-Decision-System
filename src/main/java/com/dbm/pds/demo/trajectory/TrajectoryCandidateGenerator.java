package com.dbm.pds.demo.trajectory;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.runtime.DefaultPdsRuntime;

import java.util.Arrays;
import java.util.List;

public class TrajectoryCandidateGenerator
        implements DefaultPdsRuntime.CandidateGenerator<String, Trajectory> {

    @Override
    public List<Trajectory> generate(String state, RuntimeContext ctx) {
        return Arrays.asList(
                new Trajectory("Route-A", 5, 0.9),   // short but risky
                new Trajectory("Route-B", 10, 0.1),  // long but safe
                new Trajectory("Route-C", 7, 0.3)    // balanced
        );
    }
}