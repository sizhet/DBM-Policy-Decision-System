package com.dbm.pds.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyEvaluation {

    private boolean allowed = true;
    private double scoreAdjustment = 0.0;
    private final List<PolicyNote> notes = new ArrayList<>();
    private final Map<String, Double> scoreBreakdown = new HashMap<>();

    public boolean isAllowed() {
        return allowed;
    }

    public double getScoreAdjustment() {
        return scoreAdjustment;
    }

    public List<PolicyNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public Map<String, Double> getScoreBreakdown() {
        return Collections.unmodifiableMap(scoreBreakdown);
    }

    public void reject(String policyType, String policyName, String message) {
        this.allowed = false;
        this.notes.add(new PolicyNote(policyType, policyName, message));
    }

    public void addScoreAdjustment(String policyType, String policyName, double delta, String message) {
        this.scoreAdjustment += delta;
        addBreakdown(policyType, delta);
        this.notes.add(new PolicyNote(policyType, policyName, message + " (delta=" + delta + ")"));
    }

    public void addNote(String policyType, String policyName, String message) {
        this.notes.add(new PolicyNote(policyType, policyName, message));
    }

    private void addBreakdown(String policyType, double delta) {
        Double old = scoreBreakdown.get(policyType);
        scoreBreakdown.put(policyType, old == null ? delta : old + delta);
    }
}