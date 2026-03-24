package com.dbm.pds.evidence;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.DefaultPolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class EvidenceTraceSmokeTest {

    @Test
    public void shouldBuildValidTraceWithStableHashAndReplay() {
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

        EvidenceTrace<String> trace = EvidenceTraceBuilder.build(
                input, state, rawCandidates, evaluated, chosen
        );

        Assert.assertTrue(trace.isValid());
        Assert.assertNotNull(trace.getTraceHash());
        Assert.assertEquals(64, trace.getTraceHash().length());
        Assert.assertTrue(trace.getAllowedCandidates().contains("HELLO"));
        Assert.assertEquals("HELLO", trace.getChosen());

        ReplayResult<String> replay = EvidenceTraceReplay.replay(
                trace,
                allowed -> allowed.isEmpty() ? null : allowed.get(0)
        );

        Assert.assertEquals("HELLO", replay.getOriginalChosen());
        Assert.assertEquals("HELLO", replay.getReplayChosen());
        Assert.assertTrue(replay.isMatched());
    }

    private String chooseBest(List<PolicyCandidate<String>> candidates) {
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