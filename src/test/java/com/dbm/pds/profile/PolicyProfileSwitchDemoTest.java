package com.dbm.pds.profile;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.learning.PolicyWeights;
import com.dbm.pds.learning.WeightedPolicyAggregator;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.profile.DefaultPolicyProfiles;
import com.dbm.pds.policy.profile.PolicyProfile;
import com.dbm.pds.policy.profile.PolicyProfileApplier;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PolicyProfileSwitchDemoTest {

    @Test
    public void shouldApplySafeProfileToRuntimeContext() {
        PolicyProfile profile = DefaultPolicyProfiles.safeV1();
        RuntimeContext ctx = new RuntimeContext();

        PolicyProfileApplier.applyToRuntime(profile, ctx);

        Assert.assertEquals("safe", ctx.get("policy.profile.name"));
        Assert.assertEquals("v1", ctx.get("policy.profile.version"));
        Assert.assertEquals("SAFE", ctx.get("policy.profile.mode"));
        Assert.assertEquals("prefer_short", ctx.get("policy.goal"));
    }

    @Test
    public void shouldCopyWeightsFromProfile() {
        PolicyProfile profile = DefaultPolicyProfiles.aggressiveV1();
        PolicyWeights copy = PolicyProfileApplier.copyWeights(profile);

        Assert.assertEquals(profile.getWeights().getWeight("Goal"), copy.getWeight("Goal"), 1e-9);
        Assert.assertEquals(profile.getWeights().getWeight("Risk"), copy.getWeight("Risk"), 1e-9);
    }

    @Test
    public void shouldEvaluateCandidatesUnderDifferentProfiles() {
        String safeChosen = runScenario(DefaultPolicyProfiles.safeV1());
        String aggressiveChosen = runScenario(DefaultPolicyProfiles.aggressiveV1());
        String testChosen = runScenario(DefaultPolicyProfiles.testV1());

        Assert.assertNotNull(safeChosen);
        Assert.assertNotNull(aggressiveChosen);
        Assert.assertNotNull(testChosen);

        // 最小断言：三种 profile 都应该产生合法选择
        Assert.assertTrue(
                Arrays.asList("HELLO", "HELLO!!!", "HELLO?").contains(safeChosen)
        );
        Assert.assertTrue(
                Arrays.asList("HELLO", "HELLO!!!", "HELLO?").contains(aggressiveChosen)
        );
        Assert.assertTrue(
                Arrays.asList("HELLO", "HELLO!!!", "HELLO?").contains(testChosen)
        );
    }

    private String runScenario(PolicyProfile profile) {
        RuntimeContext ctx = new RuntimeContext();
        PolicyProfileApplier.applyToRuntime(profile, ctx);

        PolicyWeights weights = PolicyProfileApplier.copyWeights(profile);

        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new WeightedPolicyAggregator(weights)
        );

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

        return chooseBest(evaluated);
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