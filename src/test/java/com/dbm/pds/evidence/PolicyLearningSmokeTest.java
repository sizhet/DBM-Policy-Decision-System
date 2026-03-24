package com.dbm.pds.evidence;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.learning.*;
import com.dbm.pds.policy.CompositePolicySystem;
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

public class PolicyLearningSmokeTest {

    @Test
    public void shouldUpdateWeightsAfterPositiveFeedback() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Risk", 1.0);
        weights.setWeight("Strategy", 1.0);
        weights.setWeight("Constraint", 1.0);

        WeightedPolicyAggregator aggregator = new WeightedPolicyAggregator(weights);

        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                aggregator
        );

        PolicyLearningLoop loop = new PolicyLearningLoop(
                weights,
                new PolicyWeightUpdater(0.1, 0.5, 2.0)
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        PolicyContext policyContext = new PolicyContext(ctx, "HELLO");
        List<PolicyCandidate<String>> evaluated = policySystem.evaluate(
                Arrays.asList("HELLO", "HELLO!!!", "HELLO?"),
                policyContext
        );

        String chosen = chooseBest(evaluated);
        Assert.assertEquals("HELLO", chosen);

        loop.applyFeedback(evaluated, chosen, new PolicyFeedback(true, 1.0));

        Assert.assertTrue(weights.getWeight("Goal") > 1.0);
        Assert.assertTrue(weights.getWeight("Strategy") > 1.0);
    }

    @Test
    public void shouldUpdateWeightsDownAfterNegativeFeedback() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Risk", 1.0);
        weights.setWeight("Strategy", 1.0);

        PolicyCandidate<String> chosen = new PolicyCandidate<>(
                "HELLO!!!",
                true,
                -2.8,
                Arrays.asList(
                        new com.dbm.pds.policy.PolicyNote("Risk", "SimpleSuffixRiskPolicy", "Penalize high-volatility suffix"),
                        new com.dbm.pds.policy.PolicyNote("Goal", "PreferShortGoalPolicy", "Prefer shorter candidates")
                )
        );

        PolicyWeightUpdater updater = new PolicyWeightUpdater(0.1, 0.5, 2.0);
        updater.update(weights, chosen, new PolicyFeedback(false, -1.0));

        Assert.assertTrue(weights.getWeight("Risk") < 1.0);
        Assert.assertTrue(weights.getWeight("Goal") < 1.0);
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