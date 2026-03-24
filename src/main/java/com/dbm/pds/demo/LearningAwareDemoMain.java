package com.dbm.pds.demo;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.evidence.EvidenceTrace;
import com.dbm.pds.evidence.EvidenceTraceBuilder;
import com.dbm.pds.evidence.EvidenceTraceMarkdownPrinter;
import com.dbm.pds.learning.*;
import com.dbm.pds.policy.*;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;

import java.util.Arrays;
import java.util.List;

public class LearningAwareDemoMain {

    public static void main(String[] args) {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Constraint", 1.0);
        weights.setWeight("Risk", 1.0);
        weights.setWeight("Strategy", 1.0);

        WeightedPolicyAggregator aggregator = new WeightedPolicyAggregator(weights);

        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                aggregator
        );

        PolicyLearningLoop learningLoop = new PolicyLearningLoop(
                weights,
                new PolicyWeightUpdater(0.1, 0.5, 2.0)
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        for (int round = 1; round <= 3; round++) {
            String input = "hello";
            String state = input.toUpperCase();

            List<String> rawCandidates = Arrays.asList(
                    state,
                    state + "!!!",
                    state + "?",
                    state + " LONG_SUFFIX"
            );

            PolicyContext policyContext = new PolicyContext(ctx, state);
            List<PolicyCandidate<String>> evaluated = policySystem.evaluate(rawCandidates, policyContext);

            String chosen = chooseBest(evaluated);

            // toy feedback: reward direct stable output
            PolicyFeedback feedback = "HELLO".equals(chosen)
                    ? new PolicyFeedback(true, 1.0)
                    : new PolicyFeedback(false, -1.0);

            learningLoop.applyFeedback(evaluated, chosen, feedback);

            EvidenceTrace<String> trace = EvidenceTraceBuilder.build(
                    input, state, rawCandidates, evaluated, chosen
            );

            System.out.println("=== Round " + round + " ===");
            System.out.println("Chosen: " + chosen);
            System.out.println("Weights: " + learningLoop.getWeights().asMap());
            System.out.println(EvidenceTraceMarkdownPrinter.print(trace));
        }
    }

    private static String chooseBest(List<PolicyCandidate<String>> candidates) {
        String best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PolicyCandidate<String> c : candidates) {
            if (!c.isAllowed()) {
                continue;
            }
            double score = c.getPolicyScoreAdjustment();
            if (best == null || score > bestScore) {
                best = c.getCandidate();
                bestScore = score;
            }
        }

        return best;
    }
}