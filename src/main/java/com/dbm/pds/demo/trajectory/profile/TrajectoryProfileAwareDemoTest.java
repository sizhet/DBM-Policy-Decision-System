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
import com.dbm.pds.policy.profile.DefaultPolicyProfiles;
import com.dbm.pds.policy.profile.PolicyProfile;
import com.dbm.pds.policy.profile.PolicyProfileApplier;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TrajectoryProfileAwareDemoTest {

    @Test
    public void shouldRunSafeProfileScenario() {
        PdsRuntimeResult<String, Trajectory> result = run(DefaultPolicyProfiles.safeV1());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getChosen());
        Assert.assertTrue(result.getEvidenceTrace().isValid());
    }

    @Test
    public void shouldRunAggressiveProfileScenario() {
        PdsRuntimeResult<String, Trajectory> result = run(DefaultPolicyProfiles.aggressiveV1());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getChosen());
        Assert.assertTrue(result.getEvidenceTrace().isValid());
    }

    @Test
    public void shouldRunTestProfileScenario() {
        PdsRuntimeResult<String, Trajectory> result = run(DefaultPolicyProfiles.testV1());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getChosen());
        Assert.assertTrue(result.getEvidenceTrace().isValid());
    }

    @Test
    public void shouldApplyProfileMetadataToRuntimeContext() {
        RuntimeContext ctx = new RuntimeContext();
        PolicyProfile profile = DefaultPolicyProfiles.safeV1();

        PolicyProfileApplier.applyToRuntime(profile, ctx);

        Assert.assertEquals("safe", ctx.get("policy.profile.name"));
        Assert.assertEquals("v1", ctx.get("policy.profile.version"));
        Assert.assertEquals("SAFE", ctx.get("policy.profile.mode"));
        Assert.assertEquals("prefer_short", ctx.get("policy.goal"));
    }

    private PdsRuntimeResult<String, Trajectory> run(PolicyProfile profile) {
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

        return runtime.run("Start→Goal", ctx);
    }
}