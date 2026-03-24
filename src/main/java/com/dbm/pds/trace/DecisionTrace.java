package com.dbm.pds.trace;

import java.util.List;

public class DecisionTrace<Y> {

    private final Object state;
    private final List<Y> candidates;
    private final List<Y> filteredCandidates;
    private final Y chosen;
    private final String policyNote;

    public DecisionTrace(Object state,
                         List<Y> candidates,
                         List<Y> filteredCandidates,
                         Y chosen,
                         String policyNote) {
        this.state = state;
        this.candidates = candidates;
        this.filteredCandidates = filteredCandidates;
        this.chosen = chosen;
        this.policyNote = policyNote;
    }

    public Object getState() { return state; }
    public List<Y> getCandidates() { return candidates; }
    public List<Y> getFilteredCandidates() { return filteredCandidates; }
    public Y getChosen() { return chosen; }
    public String getPolicyNote() { return policyNote; }
}