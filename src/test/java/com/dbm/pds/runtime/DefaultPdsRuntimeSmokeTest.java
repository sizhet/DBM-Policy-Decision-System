package com.dbm.pds.runtime;

import com.dbm.pds.api.DecisionEngine;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.DefaultPolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DefaultPdsRuntimeSmokeTest {

    @Test
    public void shouldRunFullPipelineAndProduceValidEvidenceTrace() {
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

        Assert.assertNotNull(result);
        Assert.assertEquals("hello", result.getInput());
        Assert.assertEquals("HELLO", result.getState());
        Assert.assertEquals("HELLO", result.getChosen());

        Assert.assertEquals(4, result.getRawCandidates().size());
        Assert.assertEquals(4, result.getEvaluatedCandidates().size());

        Assert.assertNotNull(result.getEvidenceTrace());
        Assert.assertTrue(result.getEvidenceTrace().isValid());
        Assert.assertNotNull(result.getEvidenceTrace().getTraceHash());
        Assert.assertEquals(64, result.getEvidenceTrace().getTraceHash().length());

        Assert.assertTrue(result.getEvidenceTrace().getAllowedCandidates().contains("HELLO"));
        Assert.assertFalse(result.getEvidenceTrace().getAllowedCandidates().contains("HELLO LONG_SUFFIX"));
    }

    @Test
    public void shouldRejectLongSuffixCandidateButKeepOtherCandidates() {
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

        boolean foundRejectedLongSuffix = false;
        boolean foundRiskPenalty = false;

        for (PolicyCandidate<String> c : result.getEvaluatedCandidates()) {
            if ("HELLO LONG_SUFFIX".equals(c.getCandidate()) && !c.isAllowed()) {
                foundRejectedLongSuffix = true;
            }
            if ("HELLO!!!".equals(c.getCandidate()) && c.isAllowed()) {
                foundRiskPenalty = c.getPolicyScoreAdjustment() < 0.0;
            }
        }

        Assert.assertTrue(foundRejectedLongSuffix);
        Assert.assertTrue(foundRiskPenalty);
    }

    private static class UppercaseStateProvider
            implements DefaultPdsRuntime.StateProvider<String, String> {
        @Override
        public String buildState(String input, RuntimeContext ctx) {
            return input == null ? null : input.toUpperCase();
        }
    }

    private static class SimpleCandidateGenerator
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

    private static class ScoreBasedDecisionEngine implements DecisionEngine<String> {
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