# ITEM P12.4
# Empirical Instantiation of PDS
### Trajectory Demo Mapping and Operationalization

## 1. From Theory to System

We map:

Theory	Runtime

S
S	StateBuilder

G
G	CandidateGenerator

D
D	Scorer + Selector

P
P	PolicyEngine
CCC	FeatureExtractor

## 2. Minimal Pipeline
Input X
 → State S(X)
 → Candidate G(s)
 → Policy P(s,C)
 → Score D
 → Decision y*
3. Trajectory Demo (Core)
Market Data
 → Trajectory Features
 → CCC Detection
 → Differential Tree Dispatch
 → Candidate Routes
 → Policy Filter
 → Scoring
 → Best Trajectory
4. Concrete Mapping
State
State s = StateBuilder.build(X, memory);
Candidate
List<Route> routes = generator.generate(s);
Policy
PolicyParams pi = policy.compute(s, routes);
routes = policy.filter(routes, pi);
Decision
Route best = routes.stream()
    .max(score(s, pi))
    .get();
5. Trajectory Score Breakdown
F(τ)=α⋅return−β⋅risk+γ⋅CCC coherence
F(τ)=α⋅return−β⋅risk+γ⋅CCC coherence
6. Profile-Aware Policy
SAFE → risk penalty ↑
AGGRESSIVE → return weight ↑
TEST → exploration ↑
7. Memory Update
Mt+1=U(Mt,st,yt,ot)
M
t+1
	​

=U(M
t
	​

,s
t
	​

,y
t
	​

,o
t
	​

)
8. Multi-Round Evolution
Round 1 → candidates
Round 2 → refined
Round 3 → converged
9. Validation Metrics
reward
stability
CCC alignment
trajectory consistency
10. Final Statement

Empirical PDS is a pipeline realization of trajectory intelligence, where theory maps directly into executable modular components.