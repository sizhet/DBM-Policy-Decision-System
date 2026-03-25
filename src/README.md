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

```bash
mvn -q -DskipTests exec:java \
  -Dexec.mainClass="com.dbm.pds.demo.PolicyAwareDemoMain"   
```

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