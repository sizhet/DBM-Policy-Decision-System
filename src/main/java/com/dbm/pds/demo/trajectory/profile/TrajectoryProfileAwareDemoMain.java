package com.dbm.pds.demo.trajectory.profile;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.demo.trajectory.TrajectoryCandidateGenerator;
import com.dbm.pds.demo.trajectory.TrajectoryDecisionEngine;
import com.dbm.pds.demo.trajectory.TrajectoryGoalPolicy;
import com.dbm.pds.demo.trajectory.TrajectoryRiskPolicy;
import com.dbm.pds.learning.PolicyWeights;
import com.dbm.pds.learning.WeightedPolicyAggregator;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.PolicyEvaluationMarkdownPrinter;
import com.dbm.pds.policy.profile.DefaultPolicyProfiles;
import com.dbm.pds.policy.profile.InMemoryPolicyRegistry;
import com.dbm.pds.policy.profile.PolicyProfile;
import com.dbm.pds.policy.profile.PolicyProfileApplier;
import com.dbm.pds.policy.profile.PolicyProfileMarkdownPrinter;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;

import java.util.Arrays;

public class TrajectoryProfileAwareDemoMain {

    public static void main(String[] args) {
        InMemoryPolicyRegistry registry = new InMemoryPolicyRegistry();
        registry.register(DefaultPolicyProfiles.safeV1());
        registry.register(DefaultPolicyProfiles.aggressiveV1());
        registry.register(DefaultPolicyProfiles.testV1());

        runScenario(registry, "safe", "v1");
        runScenario(registry, "aggressive", "v1");
        runScenario(registry, "test", "v1");
    }

    private static void runScenario(InMemoryPolicyRegistry registry, String profileName, String version) {
        registry.setActive(profileName, version);
        PolicyProfile profile = registry.getActive();

        RuntimeContext ctx = new RuntimeContext();
        PolicyProfileApplier.applyToRuntime(profile, ctx);

        PolicyWeights weights = PolicyProfileApplier.copyWeights(profile);

        DefaultPdsRuntime<String, String, Trajectory> runtime =
                new DefaultPdsRuntime<>(
                        (input, runtimeCtx) -> input,
                        new TrajectoryCandidateGenerator(),
                        new CompositePolicySystem<>(
                                Arrays.asList(new TrajectoryGoalPolicy()),
                                Arrays.asList(),
                                Arrays.asList(new TrajectoryRiskPolicy()),
                                Arrays.asList(),
                                new WeightedPolicyAggregator(weights)
                        ),
                        new TrajectoryDecisionEngine()
                );

        PdsRuntimeResult<String, Trajectory> result = runtime.run("Start→Goal", ctx);

        System.out.println("==================================================");
        System.out.println("Trajectory Profile-Aware Demo");
        System.out.println("Profile : " + profile.getQualifiedName());
        System.out.println("Mode    : " + profile.getMode());
        System.out.println("Chosen  : " + result.getChosen());
        System.out.println();

        System.out.println("## Policy Profile");
        System.out.println(PolicyProfileMarkdownPrinter.print(profile));

        System.out.println("## Policy Evaluation");
        System.out.println(PolicyEvaluationMarkdownPrinter.print(result.getEvaluatedCandidates()));

        System.out.println("## Evidence Trace");
        System.out.println(com.dbm.pds.evidence.EvidenceTraceMarkdownPrinter.print(result.getEvidenceTrace()));
        System.out.println();
    }
}