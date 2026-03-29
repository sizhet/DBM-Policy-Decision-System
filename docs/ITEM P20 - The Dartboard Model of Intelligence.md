# ITEM P20 
# Structural Intelligence via CCC Triggering
### From Dartboard Model to Metric-Space Decision Systems

## Abstract

We introduce a structural decision paradigm in which decision-making is decomposed into two phases: (1) structural triggering and (2) preference-based scoring. We formalize the notion of Common Concept Core (CCC) as a metric-space structural operator that defines admissible candidate regions prior to evaluation. This leads to a Policy Decision System (PDS) formulation in which policy reshapes the feasible decision space before optimization occurs. We demonstrate that projection-based approaches fail to preserve triggering signals, establishing CCC triggering as a geometric necessity in metric spaces. A minimal executable runtime is provided, along with a structured experimental system including policy comparison, threshold sweep, and combined grid analysis. Results show that decision outcomes emerge from the interaction between structural admissibility and policy preference rather than scoring alone.

## 1. Introduction

Most modern AI systems adopt a pipeline:

Generate → Score → Select

This implicitly assumes that all candidates are valid prior to scoring.
However, in high-dimensional or structured domains, this assumption fails.

We propose a structural paradigm:

> Decision should first determine **what is valid**, then determine **what is best**.

This leads to a two-phase architecture:

- Phase-1: structural triggering
- Phase-2: scoring and selection

## 2. Dartboard Model of Intelligence

We reinterpret decision-making as a targeting problem.

### Traditional View
- target: coordinate
- hit: proximity

### Structural View (P20)
- target: structural region
- hit: CCC activation

Formally:

Hit = Trigger(structure)

This shifts decision from coordinate space to **metric structural space**.

## 3. Common Concept Core (CCC)

CCC is defined as:

- a structural invariant in metric space
- extracted prior to projection
- used for triggering candidate validity

### 3.1 Necessity of CCC

Without CCC:

- projection reduces dimensionality
- signal is averaged out
- triggering becomes heuristic

With CCC:

- structure is preserved
- triggering becomes geometric

## 4. Metric vs Projection Theorem
### Statement

Projection-based representations cannot preserve structural triggering signals in general metric spaces.

### Implication

> CCC-based triggering is not optional — it is necessary.

## 5. Two-Phase Architecture
- Phase-1: Trigger (validity)
- Phase-2: Score (optimality)

### 5.1 Formalization
    y^* = \arg\max_{y \in C_\pi(s)} score(y \mid s, \pi)

Where:

    ( C_\pi(s) ): policy-shaped admissible set
    ( \pi ): policy

## 6. Policy Decision System (PDS)

We define:

    s = S(X)
    C(s) = G(s)
    \pi = P(s, C(s))
    y^* = D(C(s), s, \pi)

Policy modifies the candidate space:

    C_\pi(s) \subseteq C(s)

### Key Insight

Policy is not only selection — it is space deformation.

## 7. Runtime Architecture

        Input
          ↓
        State
          ↓
        Dispatch
          ↓
        Raw CCC
          ↓
        Trigger
          ↓
        Candidates
          ↓
        Score
          ↓
        Policy
          ↓
        Decision

## 8. Experimental System
### 8.1 Policy Profile Comparison
- SAFE
- AGGRESSIVE
- TEST

### 8.2 Threshold Sweep
- controls admissible region size

### 8.3 Combined Grid
- profile × threshold
- reveals decision surface

## 9. Results

Observations:

- increasing threshold reduces candidate count
- policy alters optimal choice
- combined grid shows non-linear interaction

### Key Result
Decision = Structure × Policy

## 10. Interpretation
- Phase-1 defines feasible future space
- Phase-2 ranks within that space
- projection cannot recover lost structure

## 11. Contributions
- structural decision paradigm
- CCC-triggered admissibility
- two-phase architecture
- policy-deformed decision space
- executable reference system

## 12. Implementation
- Java 8
- JUnit4
- Markdown-native outputs

## 13. Limitations
- simplified CCC extraction
- toy demonstration scale
- no large-scale benchmark yet

## 14. Future Work
- trajectory CCC expansion
- multi-agent PDS
- continuous control formulation
- integration with LLM / HLM

## 15. Conclusion

We propose a structural foundation for decision systems:

> **Validity precedes optimality.**

## One-Line Summary

> **Structure first. Decision follows.**