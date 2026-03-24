package com.dbm.pds.demo.trajectory.learning;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.demo.trajectory.TrajectoryCandidateGenerator;
import com.dbm.pds.demo.trajectory.TrajectoryGoalPolicy;
import com.dbm.pds.demo.trajectory.TrajectoryRiskPolicy;
import com.dbm.pds.learning.PolicyFeedback;
import com.dbm.pds.learning.PolicyLearningLoop;
import com.dbm.pds.learning.PolicyWeightUpdater;
import com.dbm.pds.learning.PolicyWeights;
import com.dbm.pds.learning.WeightedPolicyAggregator;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TrajectoryPolicyLearningSmokeTest {

    @Test
    public void shouldRunTrajectoryLearningLoopAndUpdateWeights() {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Risk", 0.6);
        weights.setWeight("Constraint", 1.0);
        weights.setWeight("Strategy", 1.0);

        double initialGoal = weights.getWeight("Goal");
        double initialRisk = weights.getWeight("Risk");

        PolicyLearningLoop learningLoop = new PolicyLearningLoop(
                weights,
                new PolicyWeightUpdater(0.15, 0.5, 2.0)
        );

        TrajectoryOutcomeEvaluator evaluator = new TrajectoryOutcomeEvaluator();

        Trajectory lastChosen = null;

        for (int round = 1; round <= 3; round++) {
            DefaultPdsRuntime<String, String, Trajectory> runtime =
                    new DefaultPdsRuntime<>(
                            (input, ctx) -> input,
                            new TrajectoryCandidateGenerator(),
                            new CompositePolicySystem<>(
                                    Arrays.asList(new TrajectoryGoalPolicy()),
                                    Arrays.asList(),
                                    Arrays.asList(new TrajectoryRiskPolicy()),
                                    Arrays.asList(),
                                    new WeightedPolicyAggregator(weights)
                            ),
                            new WeightedTrajectoryDecisionEngine()
                    );

            RuntimeContext ctx = new RuntimeContext();
            ctx.put("round", round);

            PdsRuntimeResult<String, Trajectory> result = runtime.run("Start→Goal", ctx);

            lastChosen = result.getChosen();
            Assert.assertNotNull(lastChosen);
            Assert.assertTrue(result.getEvidenceTrace().isValid());

            PolicyFeedback feedback = evaluator.evaluate(lastChosen);

            learningLoop.applyFeedback(
                    result.getEvaluatedCandidates(),
                    lastChosen,
                    feedback
            );
        }

        Assert.assertNotNull(lastChosen);
        Assert.assertTrue(
                weights.getWeight("Goal") != initialGoal ||
                weights.getWeight("Risk") != initialRisk
        );
    }
}