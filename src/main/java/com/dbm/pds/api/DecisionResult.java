package com.dbm.pds.api;

import java.util.List;

public class DecisionResult<Y> {

    private final Y chosen;
    private final List<Y> candidates;

    public DecisionResult(Y chosen, List<Y> candidates) {
        this.chosen = chosen;
        this.candidates = candidates;
    }

    public Y getChosen() {
        return chosen;
    }

    public List<Y> getCandidates() {
        return candidates;
    }
}