package com.dbm.pds.evidence;

import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyNote;

import java.util.ArrayList;
import java.util.List;

public final class EvidenceTraceBuilder {

    private EvidenceTraceBuilder() {
    }

    public static <Y> EvidenceTrace<Y> build(String input,
                                             String state,
                                             List<Y> rawCandidates,
                                             List<PolicyCandidate<Y>> evaluated,
                                             Y chosen) {
        List<Y> allowed = new ArrayList<>();
        List<String> notes = new ArrayList<>();

        for (PolicyCandidate<Y> c : evaluated) {
            if (c.isAllowed()) {
                allowed.add(c.getCandidate());
            }
            for (PolicyNote note : c.getNotes()) {
                notes.add(c.getCandidate() + " :: " + note.toString());
            }
        }

        List<String> violations = TraceInvariantChecker.check(
                input, state, rawCandidates, allowed, chosen
        );

        String traceHash = TraceHashing.sha256(
                input, state, rawCandidates, allowed, chosen, notes
        );

        return new EvidenceTrace<>(
                input,
                state,
                rawCandidates,
                allowed,
                chosen,
                notes,
                traceHash,
                violations
        );
    }
}