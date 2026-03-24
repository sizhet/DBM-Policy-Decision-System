package com.dbm.pds.demo;

import com.dbm.pds.api.DecisionEngine;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.evidence.EvidenceTraceMarkdownPrinter;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.DefaultPolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyEvaluationMarkdownPrinter;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;

import java.util.Arrays;
import java.util.List;

public class MinimalPdsRuntimeDemoMain {

    public static void main(String[] args) {
        DefaultPdsRuntime<String, String, String> runtime = new DefaultPdsRuntime<>(
                new UppercaseStateProvider(),
                new SimpleCandidateGenerator(),
                new CompositePolicySystem<>(
                        Arrays.asList(new PreferShortGoalPolicy()),
                        Arrays.asList(new MaxLengthConstraintPolicy(15)),
                        Arrays.asList(new SimpleSuffixRiskPolicy()),
                        Arrays.asList(new PreferFirstStrategyPolicy()),
                        new DefaultPolicyAggregator()
                ),
                new ScoreBasedDecisionEngine()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        PdsRuntimeResult<String, String> result = runtime.run("hello", ctx);

        System.out.println("=== Minimal PDS Runtime Demo ===");
        System.out.println("Input  : " + result.getInput());
        System.out.println("State  : " + result.getState());
        System.out.println("Chosen : " + result.getChosen());
        System.out.println();

        System.out.println("## Policy Evaluation");
        System.out.println(PolicyEvaluationMarkdownPrinter.print(result.getEvaluatedCandidates()));
        System.out.println();

        System.out.println("## Evidence Trace");
        System.out.println(EvidenceTraceMarkdownPrinter.print(result.getEvidenceTrace()));
    }

    public static class UppercaseStateProvider
            implements DefaultPdsRuntime.StateProvider<String, String> {
        @Override
        public String buildState(String input, RuntimeContext ctx) {
            return input == null ? null : input.toUpperCase();
        }
    }

    public static class SimpleCandidateGenerator
            implements DefaultPdsRuntime.CandidateGenerator<String, String> {
        @Override
        public List<String> generate(String state, RuntimeContext ctx) {
            return Arrays.asList(
                    state,
                    state + "!!!",
                    state + "?",
                    state + " LONG_SUFFIX"
            );
        }
    }

    public static class ScoreBasedDecisionEngine implements DecisionEngine<String> {
        @Override
        public String select(List<PolicyCandidate<String>> candidates, RuntimeContext ctx) {
            String best = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (PolicyCandidate<String> candidate : candidates) {
                if (!candidate.isAllowed()) {
                    continue;
                }

                double score = candidate.getPolicyScoreAdjustment();

                if (best == null || score > bestScore) {
                    best = candidate.getCandidate();
                    bestScore = score;
                }
            }

            return best;
        }
    }
}