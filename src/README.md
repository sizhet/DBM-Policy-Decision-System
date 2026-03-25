# ⚙ PDS Runtime — Minimal Policy Decision System Engine

This module provides a **minimal, runnable implementation** of the Policy Decision System (PDS).

It demonstrates how PDS can be instantiated as a **policy-aware decision pipeline**:

    Input → State → Candidates → Policy Evaluation → Decision

---

## 🧠 What This Runtime Shows

This is **not a production system**, but a **minimal executable skeleton** that proves:

- PDS is implementable as a modular runtime
- Policy (V) can be decomposed into:
  - Goal
  - Constraint
  - Risk
  - Strategy
- Decision is **policy-conditioned**, not purely predictive
- The system is **traceable and explainable**

---

## 🧩 Architecture Overview

    Knowledge (implicit in demo)
    ↓
    State Builder
    ↓
    Candidate Generator
    ↓
    Policy System (V)
    ├── GoalPolicy
    ├── ConstraintPolicy
    ├── RiskPolicy
    └── StrategyPolicy
    ↓
    Decision Engine
    ↓
    Chosen Output
    
---

## 🔧 Minimal Runtime Skeleton

The PDS runtime is centered around a **single orchestrator**:

> `DefaultPdsRuntime<X, S, Y>`

It defines the canonical execution pipeline of a Policy Decision System.

---

### 🧠 Core Pipeline

    Input (X)
       ↓
    State Builder (X → S)
       ↓
    Candidate Generator (S → Y[])
       ↓
    Policy System (V-layer shaping)
       ↓
    Decision Engine (selection)
       ↓
    Evidence Trace (audit / replay / hash)
       ↓
    Result (chosen + trace)

---

### ⚙ Core Components

| Component | Role |
|----------|------|
| `StateProvider` | transforms input into state |
| `CandidateGenerator` | generates future possibilities |
| `PolicySystem` | filters and reshapes candidates |
| `DecisionEngine` | selects final outcome |
| `EvidenceTrace` | provides auditability and replay |

---

### 🧩 Runtime Orchestrator

    DefaultPdsRuntime<X, S, Y>

This class connects all components into a single execution loop:

    state = buildState(input)
    candidates = generate(state)
    evaluated = policy.evaluate(candidates)
    chosen = decision.select(evaluated)
    trace = buildEvidence(...)

---

### 🔥 Key Insight

> **PDS does not map input to output.  
> It navigates a policy-shaped future space.**

---

### 🧪 Minimal Demo Entry

```bash
mvn -q -DskipTests exec:java \
  -Dexec.mainClass="com.dbm.pds.demo.MinimalPdsRuntimeDemoMain"
```
---

### 📊 Output Includes

- chosen decision
- policy evaluation table
- evidence trace
- trace hash (replayable)

---

### 🧭 Why This Matters

This skeleton proves:

- PDS is a **system architecture**, not a model
- Policy is a **first-class runtime component**
- Decision is **traceable and auditable**
- The system is **extensible by design**

---

### 🔮 Extension Points

You can plug in:

- different candidate generators (LLM / search / heuristics)
- different policy systems (rule-based / learned)
- different decision engines (score / optimization / RL)
- learning loops and policy adaptation

---

> **DefaultPdsRuntime is the "main loop" of PDS.**

---

## 🧪 Demo Scenario

Input:

    hello
    
Generated Candidates:

    HELLO
    HELLO!!!
    HELLO?
    HELLO LONG_SUFFIX
 

Policies applied:

- Goal: prefer shorter candidates
- Constraint: max length ≤ 15
- Risk: penalize "!!!"
- Strategy: prefer state-preserving candidate

---

## ▶ Run Demo

    mvn -q -DskipTests exec:java \
      -Dexec.mainClass="com.dbm.pds.demo.PolicyAwareDemoMain"   

## 🧾 Example Output

=== PDS Policy-Aware Demo ===
Input  : hello
State  : HELLO
Goal   : prefer_short
Chosen : HELLO

## 📊 Markdown Trace Output

| Candidate | Allowed | Policy Score Adjustment | Notes |
|---|---:|---:|---|
| HELLO | true | 0.5 | [Goal] PreferShortGoalPolicy - Prefer shorter candidates <br> [Strategy] PreferFirstStrategyPolicy - Prefer direct state-preserving candidate |
| HELLO!!! | true | -2.8 | [Risk] SimpleSuffixRiskPolicy - Penalize high-volatility suffix <br> [Goal] PreferShortGoalPolicy - Prefer shorter candidates |
| HELLO? | true | -0.6 | [Goal] PreferShortGoalPolicy - Prefer shorter candidates |
| HELLO LONG_SUFFIX | false | 0.0 | [Constraint] MaxLengthConstraintPolicy - Candidate length exceeds maxLength=15 |

## 🔍 What This Demonstrates

### 1. Policy Shapes the Decision Space
- Candidates are filtered and re-scored before selection
- Decision is not directly from input

### 2. Constraint ≠ Scoring
- HELLO LONG_SUFFIX is rejected outright
- It never reaches decision stage

### 3. Risk vs Goal Trade-off
- HELLO!!! is allowed but heavily penalized
- Shows soft vs hard control

### 4. Strategy Bias
- HELLO is boosted because it matches state
- Demonstrates preference shaping

## 🧪 Run Tests

    mvn test
## ✅ Included Test Coverage

#### PolicySystemSmokeTest
- Constraint rejection
- Risk penalty
- Goal preference
- Strategy preference
- Mixed policy aggregation

#### PolicyAwareDecisionEngineTest
- Final selection correctness
- Ignoring rejected candidates
- Score-based selection

#### PolicyEvaluationMarkdownPrinterTest
- Markdown formatting
- Note joining (<br>)
- Pipe escaping
- Empty notes handling

#### PolicyTraceMarkdownDemoTest
- Full pipeline verification
- Demo scenario correctness
- Markdown trace integrity

## 🔧 Key Classes

|Class	|Role
|---|---|
|CompositePolicySystem	|Orchestrates all policy plugins
|PolicyEvaluation	|Aggregates policy effects
|PolicyCandidate	|Candidate + policy result
|PolicyEvaluationMarkdownPrinter	|Trace output
|PolicyAwareDemoMain	|Demo entry point

## 🧭 Design Philosophy

Everything is replaceable. PDS is a structure, not an algorithm.

- Swap policies independently
- Replace decision engine
- Extend candidate generation
- Add trace / audit / memory later

## 🔮 Next Steps (Planned)
- Policy learning (adaptive V)
- EvidenceChain integration
- Trace → Audit → Replay
- Multi-agent PDS runtime
- Real-world demos (coding / finance / trajectory)

## 🔥 Final Insight

#### Traditional systems:

    Input → Output

#### PDS systems:

    Input → Future Space → Policy → Decision → Outcome

## 🧠 Takeaway

This runtime proves:

> **PDS is not just a concept — it is an executable decision architecture.**

---

# Evidence Trace v2

The runtime also includes a minimal **EvidenceChain / Trace v2** layer.

It supports:

- trace hash
- invariant checks
- replay verification

This makes the decision process:

- auditable
- replayable
- structurally checkable

Conceptually:

    Decision → Evidence Trace → Hash → Invariant Check → Replay

This is the first step from static PDS toward **Dynamic PDS (DPDS)**.

---

# Policy Profiles & Versioning

The runtime supports named, versioned policy profiles.

Examples:

- `safe:v1`
- `aggressive:v1`
- `test:v1`

Each profile defines:

- policy mode
- policy weights
- metadata (goal, description, etc.)

This allows the same PDS pipeline to be run under different governance styles.

Conceptually:

    PolicyProfile → RuntimeContext → Weighted Policy Evaluation → Decision

Typical modes:

- **SAFE** → stronger constraint / risk emphasis
- **AGGRESSIVE** → stronger goal / action emphasis
- **TEST** → balanced neutral baseline

---

# Trajectory + Policy Learning Demo

The runtime also includes a more realistic demo: **trajectory route selection with policy learning**.

Scenario:

- `Route-A` = short but risky
- `Route-B` = long but safe
- `Route-C` = medium balanced

The system evaluates candidate routes under:

- goal preference (shorter is better)
- risk penalty
- weighted policy aggregation

Then, after each round, feedback updates policy weights.

Conceptually:

    Trajectory Candidates
        ↓
    Policy Evaluation
        ↓
    Decision
        ↓
    Outcome Feedback
        ↓
    Policy Weight Update
        ↓
    Next Decision

This demonstrates the transition from static PDS to **Dynamic PDS (DPDS)**.

Run:

    mvn -q -DskipTests exec:java \
      -Dexec.mainClass="com.dbm.pds.demo.trajectory.learning.TrajectoryPolicyLearningDemoMain"


## Profile-Aware Trajectory Demo

The runtime also supports a **profile-aware trajectory selection demo**.

The same route candidates are evaluated under different policy profiles:

- `safe:v1`
- `aggressive:v1`
- `test:v1`

This demonstrates a core PDS principle:

> **The same future space can lead to different chosen outcomes under different policy modes.**

Scenario:

- `Route-A` = short but risky
- `Route-B` = long but safe
- `Route-C` = medium balanced

Run:

    mvn -q -DskipTests exec:java \
      -Dexec.mainClass="com.dbm.pds.demo.trajectory.profile.TrajectoryProfileAwareDemoMain"

What this shows:

- policy profiles are first-class runtime objects
- policy mode changes selection behavior
- human intent can be injected explicitly via profiles

---

## Trajectory Score Breakdown / Audit Table

Trajectory demos also support an explicit **score breakdown audit table**.

For each route, the runtime can display:

- route name
- length
- risk
- allowed / rejected
- goal contribution
- risk contribution
- strategy contribution
- final score
- policy notes

This turns trajectory selection from a black-box choice into an auditable decision process.

    Conceptually:
    
    Route Candidates
       ↓
    Policy Contributions (Goal / Risk / Strategy)
       ↓
    Final Score
       ↓
    Chosen Route

This is especially useful for:

- profile comparison (`safe` / `aggressive` / `test`)
- learning analysis
- policy debugging
- explainability demos

---
