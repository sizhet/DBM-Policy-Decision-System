package com.dbm.pds.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolicyCandidate<Y> {

    private final Y candidate;
    private final boolean allowed;
    private final double policyScoreAdjustment;
    private final List<PolicyNote> notes;

    public PolicyCandidate(Y candidate,
                           boolean allowed,
                           double policyScoreAdjustment,
                           List<PolicyNote> notes) {
        this.candidate = candidate;
        this.allowed = allowed;
        this.policyScoreAdjustment = policyScoreAdjustment;
        this.notes = new ArrayList<>(notes);
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
}