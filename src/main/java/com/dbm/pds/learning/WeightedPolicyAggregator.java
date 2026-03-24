package com.dbm.pds.learning;

import com.dbm.pds.policy.PolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyEvaluation;
import com.dbm.pds.policy.PolicyNote;

import java.util.ArrayList;
import java.util.List;

public class WeightedPolicyAggregator implements PolicyAggregator {

    private final PolicyWeights weights;

    public WeightedPolicyAggregator(PolicyWeights weights) {
        this.weights = weights;
    }

    @Override
    public <Y> PolicyCandidate<Y> aggregate(Y candidate, PolicyEvaluation evaluation) {
        double weightedAdjustment = 0.0;
        List<PolicyNote> notes = new ArrayList<>(evaluation.getNotes());

        for (PolicyNote note : notes) {
            // 这里只对带 "(delta=" 的 notes 做解释增强；真正 delta 已在 evaluation 里累计。
            // 最小版本采用 policyType 重新加权“总 adjustment”：
        }

        // 最小可运行版：对最终 adjustment 按 note types 平均加权
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

        return new PolicyCandidate<>(
                candidate,
                evaluation.isAllowed(),
                weightedAdjustment,
                notes
        );
    }
}