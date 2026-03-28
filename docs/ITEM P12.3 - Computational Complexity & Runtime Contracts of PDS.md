# ITEM P12.3
# Computational Complexity & Runtime Contracts of PDS
### From Formal System to Executable Guarantees

## 1. Motivation

Formal systems must translate into:

computability
scalability
verifiability

## 2. Complexity Decomposition

Let:

∣S∣
∣S∣: state size
∣Y∣
∣Y∣: candidate space size
T
T: horizon
B
B: branching factor
Candidate Generation
O(G)=O(B)
O(G)=O(B)
Decision
O(D)=O(∣C(s)∣)
O(D)=O(∣C(s)∣)
Trajectory Search
O(trajectory)=O(BT)
O(trajectory)=O(B
T
)
3. Structural Reduction (DBM Insight)

Using Differential Tree:

∣C(s)∣≪∣Y∣
∣C(s)∣≪∣Y∣

Thus:

O(D)≈O(log⁡∣Y∣)
O(D)≈O(log∣Y∣)
4. CCC-Based Pruning
Cπ(s)={y∣CCC(s,y)>θ}
C
π
	​

(s)={y∣CCC(s,y)>θ}
5. Runtime Contract

Define:

R=(time,memory,accuracy)
R=(time,memory,accuracy)
Contract Form
PDS⊨R(Tmax,Mmax,ϵ)
PDS⊨R(T
max
	​

,M
max
	​

,ϵ)
6. Anytime PDS

Define partial decision:

y(k)→y∗
y
(k)
→y
∗
7. Convergence Guarantee
lim⁡k→∞F(y(k))=F(y∗)
k→∞
lim
	​

F(y
(k)
)=F(y
∗
)
8. Policy-Constrained Search
Bπ≪B
B
π
	​

≪B
9. Traceability Contract（非常关键）

Each decision produces:

EvidenceChain
Trace
Policy state
CCC signals
10. Final Statement

PDS is a bounded rational system with structural pruning, enabling tractable optimization over exponentially large future spaces.