package com.dbm.pds.demo;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.*;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;

import java.util.Arrays;
import java.util.List;

public class PolicyAwareDemoMain {

    public static void main(String[] args) {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

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

        System.out.println("=== PDS Policy-Aware Demo ===");
        System.out.println("Input  : " + input);
        System.out.println("State  : " + state);
        System.out.println("Goal   : " + ctx.get("goal"));
        System.out.println("Chosen : " + chosen);
        System.out.println();

        String markdown = PolicyEvaluationMarkdownPrinter.print(evaluated);
        System.out.println(markdown);
    }

    private static String chooseBest(List<PolicyCandidate<String>> candidates) {
        String best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PolicyCandidate<String> c : candidates) {
            if (!c.isAllowed()) {
                continue;
            }

            double baseScore = 0.0;
            double finalScore = baseScore + c.getPolicyScoreAdjustment();

            if (best == null || finalScore > bestScore) {
                best = c.getCandidate();
                bestScore = finalScore;
            }
        }

        return best;
    }
}