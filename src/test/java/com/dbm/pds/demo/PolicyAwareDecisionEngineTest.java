package com.dbm.pds.demo;

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

public class PolicyAwareDecisionEngineTest {

    @Test
    public void shouldChooseHelloForPolicyAwareDemoScenario() {
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

        Assert.assertEquals("HELLO", chosen);
    }

    @Test
    public void shouldIgnoreRejectedCandidatesWhenChoosingBest() {
        List<PolicyCandidate<String>> candidates = Arrays.asList(
                new PolicyCandidate<>("A_BLOCKED", false, 100.0, Arrays.asList()),
                new PolicyCandidate<>("B_OK", true, 0.5, Arrays.asList()),
                new PolicyCandidate<>("C_OK", true, 0.2, Arrays.asList())
        );

        String chosen = chooseBest(candidates);

        Assert.assertEquals("B_OK", chosen);
    }

    @Test
    public void shouldChooseHighestPolicyAdjustedScoreAmongAllowedCandidates() {
        List<PolicyCandidate<String>> candidates = Arrays.asList(
                new PolicyCandidate<>("LOW", true, -1.0, Arrays.asList()),
                new PolicyCandidate<>("MID", true, 0.0, Arrays.asList()),
                new PolicyCandidate<>("HIGH", true, 1.25, Arrays.asList())
        );

        String chosen = chooseBest(candidates);

        Assert.assertEquals("HIGH", chosen);
    }

    @Test
    public void shouldReturnNullWhenAllCandidatesAreRejected() {
        List<PolicyCandidate<String>> candidates = Arrays.asList(
                new PolicyCandidate<>("A", false, 10.0, Arrays.asList()),
                new PolicyCandidate<>("B", false, 20.0, Arrays.asList())
        );

        String chosen = chooseBest(candidates);

        Assert.assertNull(chosen);
    }

    private String chooseBest(List<PolicyCandidate<String>> candidates) {
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