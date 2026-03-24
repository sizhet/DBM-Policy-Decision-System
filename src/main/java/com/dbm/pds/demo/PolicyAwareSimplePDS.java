package com.dbm.pds.demo;

import com.dbm.pds.api.DecisionResult;
import com.dbm.pds.api.PolicyDecisionSystem;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.policy.*;
import com.dbm.pds.policy.constraint.MaxLengthConstraintPolicy;
import com.dbm.pds.policy.goal.PreferShortGoalPolicy;
import com.dbm.pds.policy.risk.SimpleSuffixRiskPolicy;
import com.dbm.pds.policy.strategy.PreferFirstStrategyPolicy;

import java.util.Arrays;
import java.util.List;

public class PolicyAwareSimplePDS implements PolicyDecisionSystem<String, String> {

    private final CompositePolicySystem<String> policySystem;
    private final SimplePolicyAwareDecisionEngine decisionEngine;

    public PolicyAwareSimplePDS() {
        this.policySystem = new CompositePolicySystem<>(
                Arrays.asList(new PreferShortGoalPolicy()),
                Arrays.asList(new MaxLengthConstraintPolicy(15)),
                Arrays.asList(new SimpleSuffixRiskPolicy()),
                Arrays.asList(new PreferFirstStrategyPolicy()),
                new DefaultPolicyAggregator()
        );
        this.decisionEngine = new SimplePolicyAwareDecisionEngine();
    }

    @Override
    public DecisionResult<String> decide(String input, RuntimeContext ctx) {
        String state = input.toUpperCase();

        List<String> rawCandidates = Arrays.asList(
                state,
                state + "!!!",
                state + "?",
                state + " LONG_SUFFIX"
        );

        PolicyContext policyContext = new PolicyContext(ctx, state);
        List<PolicyCandidate<String>> evaluated = policySystem.evaluate(rawCandidates, policyContext);

        String chosen = decisionEngine.select(evaluated, ctx);

        return new DecisionResult<>(chosen, rawCandidates);
    }
}