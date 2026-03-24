package com.dbm.pds.core;

import com.dbm.pds.api.*;

import java.util.List;

public class DefaultPDS<X, S, Y> implements PolicyDecisionSystem<X, Y> {

    private final KnowledgeModel<X, Y> knowledge;
    private final StateProvider<X, S> stateProvider;
    private final CandidateGenerator<S, Y> generator;
    private final DecisionEngine<Y> decision;
    private final PolicySystem<Y> policy;

    public DefaultPDS(KnowledgeModel<X, Y> knowledge,
                      StateProvider<X, S> stateProvider,
                      CandidateGenerator<S, Y> generator,
                      DecisionEngine<Y> decision,
                      PolicySystem<Y> policy) {
        this.knowledge = knowledge;
        this.stateProvider = stateProvider;
        this.generator = generator;
        this.decision = decision;
        this.policy = policy;
    }

    @Override
    public DecisionResult<Y> decide(X input, RuntimeContext ctx) {

        S state = stateProvider.buildState(input, ctx);

        List<Y> candidates = generator.generate(state, ctx);

        List<Y> filtered = policy.applyPolicy(candidates, ctx);

        Y selected = decision.select(filtered, ctx);

        return new DecisionResult<>(selected, filtered);
    }
}