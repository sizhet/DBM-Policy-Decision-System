package com.dbm.pds.learning;

import com.dbm.pds.policy.PolicyCandidate;

import java.util.List;

public class PolicyLearningLoop {

    private final PolicyWeights weights;
    private final PolicyWeightUpdater updater;

    public PolicyLearningLoop(PolicyWeights weights, PolicyWeightUpdater updater) {
        this.weights = weights;
        this.updater = updater;
    }

    public PolicyWeights getWeights() {
        return weights;
    }

    public <Y> void applyFeedback(List<PolicyCandidate<Y>> evaluated,
                                  Y chosen,
                                  PolicyFeedback feedback) {
        PolicyCandidate<Y> chosenCandidate = null;
        for (PolicyCandidate<Y> c : evaluated) {
            if (chosen == null) {
                break;
            }
            if (chosen.equals(c.getCandidate())) {
                chosenCandidate = c;
                break;
            }
        }
        updater.update(weights, chosenCandidate, feedback);
    }
}