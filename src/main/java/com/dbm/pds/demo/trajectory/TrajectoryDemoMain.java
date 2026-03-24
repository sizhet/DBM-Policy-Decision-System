package com.dbm.pds.demo.trajectory;

import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.evidence.EvidenceTraceMarkdownPrinter;
import com.dbm.pds.policy.CompositePolicySystem;
import com.dbm.pds.policy.DefaultPolicyAggregator;
import com.dbm.pds.policy.PolicyEvaluationMarkdownPrinter;
import com.dbm.pds.runtime.DefaultPdsRuntime;
import com.dbm.pds.runtime.PdsRuntimeResult;

import java.util.Arrays;

public class TrajectoryDemoMain {

    public static void main(String[] args) {

        DefaultPdsRuntime<String, String, Trajectory> runtime =
                new DefaultPdsRuntime<>(
                        (input, ctx) -> input,
                        new TrajectoryCandidateGenerator(),
                        new CompositePolicySystem<>(
                                Arrays.asList(new TrajectoryGoalPolicy()),
                                Arrays.asList(), // no hard constraints
                                Arrays.asList(new TrajectoryRiskPolicy()),
                                Arrays.asList(),
                                new DefaultPolicyAggregator()
                        ),
                        new TrajectoryDecisionEngine()
                );

        RuntimeContext ctx = new RuntimeContext();

        PdsRuntimeResult<String, Trajectory> result =
                runtime.run("Start→Goal", ctx);

        System.out.println("=== Trajectory PDS Demo ===");
        System.out.println("Chosen: " + result.getChosen());
        System.out.println();

        System.out.println("## Policy Evaluation");
        System.out.println(
                PolicyEvaluationMarkdownPrinter.print(result.getEvaluatedCandidates())
        );

        System.out.println("## Evidence Trace");
        System.out.println(
                EvidenceTraceMarkdownPrinter.print(result.getEvidenceTrace())
        );
    }
}