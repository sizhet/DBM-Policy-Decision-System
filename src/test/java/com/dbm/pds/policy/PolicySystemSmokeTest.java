package com.dbm.pds.policy;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.*;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PolicySystemSmokeTest {

    @Test
    public void shouldRejectCandidateByConstraintPolicy() {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(10)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        PolicyContext policyContext = new PolicyContext(ctx, "HELLO");

        List<PolicyCandidate<String>> results = policySystem.evaluate(
                Arrays.asList("HELLO", "HELLO WORLD TOO LONG"),
                policyContext
        );

        Assert.assertEquals(2, results.size());

        PolicyCandidate<String> shortCandidate = results.get(0);
        PolicyCandidate<String> longCandidate = results.get(1);

        Assert.assertTrue(shortCandidate.isAllowed());
        Assert.assertFalse(longCandidate.isAllowed());
        Assert.assertTrue(
                containsMessage(longCandidate, "maxLength=10")
        );
    }

    @Test
    public void shouldApplyRiskPenaltyToVolatileSuffix() {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(30)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        PolicyContext policyContext = new PolicyContext(ctx, "HELLO");

        List<PolicyCandidate<String>> results = policySystem.evaluate(
                Arrays.asList("HELLO", "HELLO!!!"),
                policyContext
        );

        Assert.assertEquals(2, results.size());

        PolicyCandidate<String> calm = results.get(0);
        PolicyCandidate<String> risky = results.get(1);

        Assert.assertTrue(calm.isAllowed());
        Assert.assertTrue(risky.isAllowed());

        Assert.assertTrue(
                risky.getPolicyScoreAdjustment() < calm.getPolicyScoreAdjustment()
        );
        Assert.assertTrue(
                containsPolicyType(risky, "Risk")
        );
    }

    @Test
    public void shouldPreferShorterCandidateWhenGoalIsPreferShort() {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(30)),
                Arrays.asList(),
                Arrays.asList(),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        PolicyContext policyContext = new PolicyContext(ctx, "HELLO");

        List<PolicyCandidate<String>> results = policySystem.evaluate(
                Arrays.asList("HELLO", "HELLO WORLD"),
                policyContext
        );

        PolicyCandidate<String> shortCandidate = results.get(0);
        PolicyCandidate<String> longCandidate = results.get(1);

        Assert.assertTrue(shortCandidate.isAllowed());
        Assert.assertTrue(longCandidate.isAllowed());

        Assert.assertTrue(
                shortCandidate.getPolicyScoreAdjustment() > longCandidate.getPolicyScoreAdjustment()
        );
        Assert.assertTrue(
                containsPolicyType(shortCandidate, "Goal")
        );
    }

    @Test
    public void shouldPreferStatePreservingCandidateByStrategyPolicy() {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(),
                Arrays.asList(new MaxLengthConstraintPolicy(30)),
                Arrays.asList(),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        PolicyContext policyContext = new PolicyContext(ctx, "HELLO");

        List<PolicyCandidate<String>> results = policySystem.evaluate(
                Arrays.asList("HELLO", "HELLO?"),
                policyContext
        );

        PolicyCandidate<String> direct = results.get(0);
        PolicyCandidate<String> altered = results.get(1);

        Assert.assertTrue(direct.isAllowed());
        Assert.assertTrue(altered.isAllowed());

        Assert.assertTrue(
                direct.getPolicyScoreAdjustment() > altered.getPolicyScoreAdjustment()
        );
        Assert.assertTrue(
                containsPolicyType(direct, "Strategy")
        );
    }

    @Test
    public void shouldAggregateMixedPoliciesCorrectly() {
        CompositePolicySystem<String> policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );

        RuntimeContext ctx = new RuntimeContext();
        ctx.put("goal", "prefer_short");

        String state = "HELLO";
        PolicyContext policyContext = new PolicyContext(ctx, state);

        List<PolicyCandidate<String>> results = policySystem.evaluate(
                Arrays.asList(
                        "HELLO",
                        "HELLO!!!",
                        "HELLO?",
                        "HELLO LONG_SUFFIX"
                ),
                policyContext
        );

        Assert.assertEquals(4, results.size());

        PolicyCandidate<String> exact = results.get(0);
        PolicyCandidate<String> risky = results.get(1);
        PolicyCandidate<String> mild = results.get(2);
        PolicyCandidate<String> tooLong = results.get(3);

        Assert.assertTrue(exact.isAllowed());
        Assert.assertTrue(risky.isAllowed());
        Assert.assertTrue(mild.isAllowed());
        Assert.assertFalse(tooLong.isAllowed());

        Assert.assertTrue(exact.getPolicyScoreAdjustment() > risky.getPolicyScoreAdjustment());
        Assert.assertTrue(containsPolicyType(exact, "Goal"));
        Assert.assertTrue(containsPolicyType(exact, "Strategy"));
        Assert.assertTrue(containsPolicyType(risky, "Risk"));
        Assert.assertTrue(containsPolicyType(tooLong, "Constraint"));
    }

    private boolean containsPolicyType(PolicyCandidate<String> candidate, String policyType) {
        for (PolicyNote note : candidate.getNotes()) {
            if (policyType.equals(note.getPolicyType())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMessage(PolicyCandidate<String> candidate, String fragment) {
        for (PolicyNote note : candidate.getNotes()) {
            if (note.getMessage() != null && note.getMessage().contains(fragment)) {
                return true;
            }
        }
        return false;
    }
}