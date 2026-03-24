# ITEM P06
# Minimal PDS Runtime — Java Skeleton & Plugin Architecture

（最小 PDS 运行时——Java骨架与插件架构）

## 1. Design Principles（设计原则）
插件化（Plug-in first）
可替换（Replaceable modules）
可观测（Trace / Evidence）
与 DBM runtime 对齐

## 2. Core Interface（核心接口）
package com.dbm.pds.api;

import java.util.List;
import java.util.Map;

public interface PolicyDecisionSystem<X, Y> {

    DecisionResult<Y> decide(X input, RuntimeContext ctx);

}

## 3. Runtime Context（统一上下文）
package com.dbm.pds.api;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private final Map<String, Object> runtime = new HashMap<>();

    public void put(String key, Object value) {
        runtime.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) runtime.get(key);
    }

}

## 4. Five Pillars Interfaces（五大模块接口）
### 4.1 Knowledge Model (I)
public interface KnowledgeModel<X, Y> {

    Y infer(X input, RuntimeContext ctx);

}

### 4.2 State / Trajectory (II)
public interface StateProvider<X, S> {

    S buildState(X input, RuntimeContext ctx);

}

### 4.3 Candidate Generator (III)
import java.util.List;

public interface CandidateGenerator<S, Y> {

    List<Y> generate(S state, RuntimeContext ctx);

}

### 4.4 Decision Engine (IV)
import java.util.List;

public interface DecisionEngine<Y> {

    Y select(List<Y> candidates, RuntimeContext ctx);

}

### 4.5 Policy System (V)
import java.util.List;

public interface PolicySystem<Y> {

    List<Y> applyPolicy(List<Y> candidates, RuntimeContext ctx);

}

## 5. Default Orchestrator（核心编排器）
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

## 6. Decision Result（输出结构）
package com.dbm.pds.api;

import java.util.List;

public class DecisionResult<Y> {

    private final Y chosen;
    private final List<Y> candidates;

    public DecisionResult(Y chosen, List<Y> candidates) {
        this.chosen = chosen;
        this.candidates = candidates;
    }

    public Y getChosen() {
        return chosen;
    }

    public List<Y> getCandidates() {
        return candidates;
    }
}

## 7. Extension Hooks（扩展点）

建议后续加入：

EvidenceChainRecorder
PolicyAudit
TrajectoryTrace
MemoryRegistry
🔥 核心设计理念

Everything is replaceable — PDS is a structure, not an algorithm.