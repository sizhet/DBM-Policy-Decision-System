package com.dbm.pds.demo.trajectory.learning;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.demo.trajectory.TrajectoryCandidateGenerator;
import com.dbm.pds.demo.trajectory.TrajectoryGoalPolicy;
import com.dbm.pds.demo.trajectory.TrajectoryRiskPolicy;
import com.dbm.pds.evidence.EvidenceTraceMarkdownPrinter;
import com.dbm.pds.learning.PolicyFeedback;
import com.dbm.pds.learning.PolicyLearningLoop;
import com.dbm.pds.learning.PolicyWeightUpdater;
import com.dbm.pds.learning.PolicyWeights;
import com.dbm.pds.learning.WeightedPolicyAggregator;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyEvaluationMarkdownPrinter;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;

import java.util.Arrays;
import java.util.List;

public class TrajectoryPolicyLearningDemoMain {

    public static void main(String[] args) {
        PolicyWeights weights = new PolicyWeights();
        weights.setWeight("Goal", 1.0);
        weights.setWeight("Risk", 0.6);
        weights.setWeight("Constraint", 1.0);
        weights.setWeight("Strategy", 1.0);

        PolicyLearningLoop learningLoop = new PolicyLearningLoop(
                weights,
                new PolicyWeightUpdater(0.15, 0.5, 2.0)
        );

        TrajectoryOutcomeEvaluator outcomeEvaluator = new TrajectoryOutcomeEvaluator();

        for (int round = 1; round <= 5; round++) {
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

            Trajectory chosen = result.getChosen();
            PolicyFeedback feedback = outcomeEvaluator.evaluate(chosen);

            learningLoop.applyFeedback(
                    result.getEvaluatedCandidates(),
                    chosen,
                    feedback
            );

            System.out.println("=== Trajectory Learning Round " + round + " ===");
            System.out.println("Chosen   : " + chosen);
            System.out.println("Feedback : success=" + feedback.isSuccess()
                    + ", reward=" + feedback.getReward());
            System.out.println("Weights  : " + learningLoop.getWeights().asMap());
            System.out.println();

            System.out.println("## Policy Evaluation");
            System.out.println(PolicyEvaluationMarkdownPrinter.print(result.getEvaluatedCandidates()));
            System.out.println();

            System.out.println("## Evidence Trace");
            System.out.println(EvidenceTraceMarkdownPrinter.print(result.getEvidenceTrace()));
            System.out.println();

            System.out.println("## Score Breakdown Audit");
            System.out.println(
                    com.dbm.pds.demo.trajectory.audit.TrajectoryScoreBreakdownMarkdownPrinter
                            .print(result.getEvaluatedCandidates())
            );
            System.out.println();
        }
    }
}