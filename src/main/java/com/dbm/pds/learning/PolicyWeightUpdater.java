package com.dbm.pds.learning;

import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyNote;

public class PolicyWeightUpdater {

    private final double learningRate;
    private final double minWeight;
    private final double maxWeight;

    public PolicyWeightUpdater(double learningRate, double minWeight, double maxWeight) {
        this.learningRate = learningRate;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public <Y> void update(PolicyWeights weights,
                           PolicyCandidate<Y> chosenCandidate,
                           PolicyFeedback feedback) {
        if (chosenCandidate == null) {
            return;
        }

        double signedStep = learningRate * feedback.getReward();

        for (PolicyNote note : chosenCandidate.getNotes()) {
            String policyType = note.getPolicyType();
            double oldWeight = weights.getWeight(policyType);
            double newWeight = clamp(oldWeight + signedStep, minWeight, maxWeight);
            weights.setWeight(policyType, newWeight);
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}