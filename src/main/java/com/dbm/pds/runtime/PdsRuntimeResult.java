package com.dbm.pds.runtime;

import com.dbm.pds.evidence.EvidenceTrace;
import com.dbm.pds.policy.PolicyCandidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdsRuntimeResult<S, Y> {

    private final Object input;
    private final S state;
    private final List<Y> rawCandidates;
    private final List<PolicyCandidate<Y>> evaluatedCandidates;
    private final Y chosen;
    private final EvidenceTrace<Y> evidenceTrace;

    public PdsRuntimeResult(Object input,
                            S state,
                            List<Y> rawCandidates,
                            List<PolicyCandidate<Y>> evaluatedCandidates,
                            Y chosen,
                            EvidenceTrace<Y> evidenceTrace) {
        this.input = input;
        this.state = state;
        this.rawCandidates = new ArrayList<>(rawCandidates);
        this.evaluatedCandidates = new ArrayList<>(evaluatedCandidates);
        this.chosen = chosen;
        this.evidenceTrace = evidenceTrace;
    }

    public Object getInput() {
        return input;
    }

    public S getState() {
        return state;
    }

    public List<Y> getRawCandidates() {
        return Collections.unmodifiableList(rawCandidates);
    }

    public List<PolicyCandidate<Y>> getEvaluatedCandidates() {
        return Collections.unmodifiableList(evaluatedCandidates);
    }

    public Y getChosen() {
        return chosen;
    }

    public EvidenceTrace<Y> getEvidenceTrace() {
        return evidenceTrace;
    }
}