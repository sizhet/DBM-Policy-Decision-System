package com.dbm.pds.policy;

import com.dbm.pds.policy.constraint.ConstraintPolicy;
import com.dbm.pds.policy.goal.GoalPolicy;
import com.dbm.pds.policy.risk.RiskPolicy;
import com.dbm.pds.policy.strategy.StrategyPolicy;

import java.util.ArrayList;
import java.util.List;

public class CompositePolicySystem<Y> implements PolicySystem<Y> {

    private final List<GoalPolicy<Y>> goalPolicies;
    private final List<ConstraintPolicy<Y>> constraintPolicies;
    private final List<RiskPolicy<Y>> riskPolicies;
    private final List<StrategyPolicy<Y>> strategyPolicies;
    private final PolicyAggregator aggregator;

    public CompositePolicySystem(List<GoalPolicy<Y>> goalPolicies,
                                 List<ConstraintPolicy<Y>> constraintPolicies,
                                 List<RiskPolicy<Y>> riskPolicies,
                                 List<StrategyPolicy<Y>> strategyPolicies,
                                 PolicyAggregator aggregator) {
        this.goalPolicies = goalPolicies;
        this.constraintPolicies = constraintPolicies;
        this.riskPolicies = riskPolicies;
        this.strategyPolicies = strategyPolicies;
        this.aggregator = aggregator;
    }

    @Override
    public List<PolicyCandidate<Y>> evaluate(List<Y> candidates, PolicyContext context) {
        List<PolicyCandidate<Y>> out = new ArrayList<>();
        for (Y candidate : candidates) {
            PolicyEvaluation evaluation = new PolicyEvaluation();

            for (ConstraintPolicy<Y> policy : constraintPolicies) {
                policy.evaluate(candidate, context, evaluation);
                if (!evaluation.isAllowed()) {
                    break;
                }
            }

            if (evaluation.isAllowed()) {
                for (RiskPolicy<Y> policy : riskPolicies) {
                    policy.evaluate(candidate, context, evaluation);
                    if (!evaluation.isAllowed()) {
                        break;
                    }
                }
            }

            if (evaluation.isAllowed()) {
                for (GoalPolicy<Y> policy : goalPolicies) {
                    policy.evaluate(candidate, context, evaluation);
                }
                for (StrategyPolicy<Y> policy : strategyPolicies) {
                    policy.evaluate(candidate, context, evaluation);
                }
            }

            out.add(aggregator.aggregate(candidate, evaluation));
        }
        return out;
    }
}