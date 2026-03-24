package com.dbm.pds.demo;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.learning.PolicyWeights;
import com.dbm.pds.learning.WeightedPolicyAggregator;
import com.dbm.pds.policy.*;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.profile.*;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;

import java.util.Arrays;
import java.util.List;

public class PolicyProfileSwitchDemoMain {

    public static void main(String[] args) {
        InMemoryPolicyRegistry registry = new InMemoryPolicyRegistry();
        registry.register(DefaultPolicyProfiles.safeV1());
        registry.register(DefaultPolicyProfiles.aggressiveV1());
        registry.register(DefaultPolicyProfiles.testV1());

        runScenario(registry, "safe", "v1");
        runScenario(registry, "aggressive", "v1");
        runScenario(registry, "test", "v1");
    }

    private static void runScenario(InMemoryPolicyRegistry registry, String name, String version) {
        registry.setActive(name, version);
        PolicyProfile profile = registry.getActive();

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
        String chosen = chooseBest(evaluated);

        System.out.println("=== Profile: " + profile.getQualifiedName() + " ===");
        System.out.println("Mode   : " + profile.getMode());
        System.out.println("Chosen : " + chosen);
        System.out.println(PolicyProfileMarkdownPrinter.print(profile));
        System.out.println(PolicyEvaluationMarkdownPrinter.print(evaluated));
        System.out.println();
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