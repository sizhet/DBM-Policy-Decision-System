package com.dbm.pds.evidence;

import java.util.ArrayList;
import java.util.List;

public final class TraceInvariantChecker {

    private TraceInvariantChecker() {
    }

    public static <Y> List<String> check(String input,
                                         String state,
                                         List<Y> rawCandidates,
                                         List<Y> allowedCandidates,
                                         Y chosen) {
        List<String> violations = new ArrayList<>();

        if (input == null) {
            violations.add("Input must not be null");
        }

        if (state == null) {
            violations.add("State must not be null");
        }

        if (rawCandidates == null || rawCandidates.isEmpty()) {
            violations.add("Raw candidates must not be null or empty");
        }

        if (allowedCandidates == null) {
            violations.add("Allowed candidates must not be null");
        }

        if (chosen != null && !allowedCandidates.contains(chosen)) {
            violations.add("Chosen candidate must belong to allowed candidates");
        }

        if (chosen == null && !allowedCandidates.isEmpty()) {
            violations.add("Chosen candidate is null while allowed candidates are non-empty");
        }

        return violations;
    }
}