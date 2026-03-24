package com.dbm.pds.demo;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.DefaultPolicyAggregator;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicyEvaluationMarkdownPrinter;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PolicyTraceMarkdownDemoTest {

    @Test
    public void shouldProduceExpectedMarkdownTraceForDemoScenario() {
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
        String markdown = PolicyEvaluationMarkdownPrinter.print(evaluated);

        Assert.assertEquals("HELLO", chosen);

        Assert.assertTrue(markdown.contains("| Candidate | Allowed | Policy Score Adjustment | Notes |"));
        Assert.assertTrue(markdown.contains("| HELLO | true |"));
        Assert.assertTrue(markdown.contains("| HELLO!!! | true |"));
        Assert.assertTrue(markdown.contains("| HELLO? | true |"));
        Assert.assertTrue(markdown.contains("| HELLO LONG_SUFFIX | false |"));

        Assert.assertTrue(markdown.contains("Constraint"));
        Assert.assertTrue(markdown.contains("Risk"));
        Assert.assertTrue(markdown.contains("Goal"));
        Assert.assertTrue(markdown.contains("Strategy"));

        Assert.assertTrue(markdown.contains("PreferShortGoalPolicy"));
        Assert.assertTrue(markdown.contains("SimpleSuffixRiskPolicy"));
        Assert.assertTrue(markdown.contains("MaxLengthConstraintPolicy"));
        Assert.assertTrue(markdown.contains("PreferFirstStrategyPolicy"));
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