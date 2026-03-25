package com.dbm.pds.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyCandidate<Y> {

    private final Y candidate;
    private final boolean allowed;
    private final double policyScoreAdjustment;
    private final List<PolicyNote> notes;
    private final Map<String, Double> scoreBreakdown;

    public PolicyCandidate(Y candidate,
                           boolean allowed,
                           double policyScoreAdjustment,
                           List<PolicyNote> notes) {
        this(candidate, allowed, policyScoreAdjustment, notes, Collections.<String, Double>emptyMap());
    }

    public PolicyCandidate(Y candidate,
                           boolean allowed,
                           double policyScoreAdjustment,
                           List<PolicyNote> notes,
                           Map<String, Double> scoreBreakdown) {
        this.candidate = candidate;
        this.allowed = allowed;
        this.policyScoreAdjustment = policyScoreAdjustment;
        this.notes = new ArrayList<>(notes);
        this.scoreBreakdown = new HashMap<>(scoreBreakdown);
    }

    public Y getCandidate() {
        return candidate;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public double getPolicyScoreAdjustment() {
        return policyScoreAdjustment;
    }

    public List<PolicyNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public Map<String, Double> getScoreBreakdown() {
        return Collections.unmodifiableMap(scoreBreakdown);
    }

    public double getBreakdownValue(String policyType) {
        Double value = scoreBreakdown.get(policyType);
        return value == null ? 0.0 : value;
    }
}