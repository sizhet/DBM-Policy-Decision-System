package com.dbm.pds.evidence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvidenceTrace<Y> {

    private final String input;
    private final String state;
    private final List<Y> rawCandidates;
    private final List<Y> allowedCandidates;
    private final Y chosen;
    private final List<String> notes;
    private final String traceHash;
    private final List<String> invariantViolations;

    public EvidenceTrace(String input,
                         String state,
                         List<Y> rawCandidates,
                         List<Y> allowedCandidates,
                         Y chosen,
                         List<String> notes,
                         String traceHash,
                         List<String> invariantViolations) {
        this.input = input;
        this.state = state;
        this.rawCandidates = new ArrayList<>(rawCandidates);
        this.allowedCandidates = new ArrayList<>(allowedCandidates);
        this.chosen = chosen;
        this.notes = new ArrayList<>(notes);
        this.traceHash = traceHash;
        this.invariantViolations = new ArrayList<>(invariantViolations);
    }

    public String getInput() {
        return input;
    }

    public String getState() {
        return state;
    }

    public List<Y> getRawCandidates() {
        return Collections.unmodifiableList(rawCandidates);
    }

    public List<Y> getAllowedCandidates() {
        return Collections.unmodifiableList(allowedCandidates);
    }

    public Y getChosen() {
        return chosen;
    }

    public List<String> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public String getTraceHash() {
        return traceHash;
    }

    public List<String> getInvariantViolations() {
        return Collections.unmodifiableList(invariantViolations);
    }

    public boolean isValid() {
        return invariantViolations.isEmpty();
    }
}