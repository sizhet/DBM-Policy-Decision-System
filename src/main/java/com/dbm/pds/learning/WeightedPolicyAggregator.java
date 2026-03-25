package com.dbm.pds.learning;

import com.dbm.pds.policy.PolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyEvaluation;
import com.dbm.pds.policy.PolicyNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightedPolicyAggregator implements PolicyAggregator {

    private final PolicyWeights weights;

    public WeightedPolicyAggregator(PolicyWeights weights) {
        this.weights = weights;
    }

    @Override
    public <Y> PolicyCandidate<Y> aggregate(Y candidate, PolicyEvaluation evaluation) {
        double weightedAdjustment;
        List<PolicyNote> notes = new ArrayList<>(evaluation.getNotes());

        double baseAdjustment = evaluation.getScoreAdjustment();

        if (!notes.isEmpty()) {
            double factorSum = 0.0;
            for (PolicyNote note : notes) {
                factorSum += weights.getWeight(note.getPolicyType());
            }
            double avgFactor = factorSum / notes.size();
            weightedAdjustment = baseAdjustment * avgFactor;
        } else {
            weightedAdjustment = baseAdjustment;
        }

        Map<String, Double> weightedBreakdown = new HashMap<>();
        for (Map.Entry<String, Double> e : evaluation.getScoreBreakdown().entrySet()) {
            double factor = weights.getWeight(e.getKey());
            weightedBreakdown.put(e.getKey(), e.getValue() * factor);
        }

        return new PolicyCandidate<>(
                candidate,
                evaluation.isAllowed(),
                weightedAdjustment,
                notes,
                weightedBreakdown
        );
    }
}