package com.dbm.pds.runtime;

import com.dbm.pds.api.DecisionEngine;
import com.dbm.pds.api.RuntimeContext;
import com.dbm.pds.evidence.EvidenceTrace;
import com.dbm.pds.evidence.EvidenceTraceBuilder;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyContext;
import com.dbm.pds.policy.PolicySystem;

import java.util.List;

public class DefaultPdsRuntime<X, S, Y> {

    public interface StateProvider<X, S> {
        S buildState(X input, RuntimeContext ctx);
    }

    public interface CandidateGenerator<S, Y> {
        List<Y> generate(S state, RuntimeContext ctx);
    }

    private final StateProvider<X, S> stateProvider;
    private final CandidateGenerator<S, Y> candidateGenerator;
    private final PolicySystem<Y> policySystem;
    private final DecisionEngine<Y> decisionEngine;

    public DefaultPdsRuntime(StateProvider<X, S> stateProvider,
                             CandidateGenerator<S, Y> candidateGenerator,
                             PolicySystem<Y> policySystem,
                             DecisionEngine<Y> decisionEngine) {
        this.stateProvider = stateProvider;
        this.candidateGenerator = candidateGenerator;
        this.policySystem = policySystem;
        this.decisionEngine = decisionEngine;
    }

    public PdsRuntimeResult<S, Y> run(X input, RuntimeContext ctx) {
        S state = stateProvider.buildState(input, ctx);

        List<Y> rawCandidates = candidateGenerator.generate(state, ctx);

        PolicyContext policyContext = new PolicyContext(ctx, state);
        List<PolicyCandidate<Y>> evaluatedCandidates = policySystem.evaluate(rawCandidates, policyContext);

        Y chosen = decisionEngine.select(evaluatedCandidates, ctx);

        EvidenceTrace<Y> evidenceTrace = EvidenceTraceBuilder.build(
                input == null ? null : String.valueOf(input),
                state == null ? null : String.valueOf(state),
                rawCandidates,
                evaluatedCandidates,
                chosen
        );

        return new PdsRuntimeResult<>(
                input,
                state,
                rawCandidates,
                evaluatedCandidates,
                chosen,
                evidenceTrace
        );
    }
}