# ITEM P12
# Formalization of PDS — Mathematical Expression of Policy Decision Systems

（PDS 的数学化表达）

## 1. Motivation（动机）

如果 PDS 只停留在概念层，就难以：

做理论推导
做 benchmark
做 runtime contract
做跨系统对比

因此需要一个最小数学骨架。

## 2. Basic Objects（基本对象）

我们定义：

X
X: input / observation / state history
S
S: structured state space
Y
Y: candidate outcome space
K
K: knowledge model
D
D: decision engine
P
P: policy system

## 3. Five-Pillar Formalization（五支柱形式化）
Pillar I — Knowledge

知识模型定义为：

K:X→R
K:X→R

其中 
R
R 是内部表示空间，可为：

score distribution
structural embedding
CCC representation
predictive mapping

在较强形式下，也可以写为：

K(X)=f^(X)
K(X)=
f
^
	​

(X)

表示系统对输入的内在解释能力。

Pillar II — State Construction

状态构造器：

S:X→S
S:X→S

它将原始输入 
X
X 映射为决策所需状态：

s=S(X)
s=S(X)

其中 
s∈S
s∈S 可以包含：

当前状态
历史轨迹
外部上下文
memory summary
Pillar III — Candidate Generator

候选生成器：

G:S→2Y
G:S→2
Y

其中 
2Y
2
Y
 表示候选集合。

即：

C(s)=G(s)={y1,y2,…,yn}
C(s)=G(s)={y
1
	​

,y
2
	​

,…,y
n
	​

}
Pillar IV — Decision Engine

决策引擎定义为对候选的评分与选择：

D:2Y×S×Π→Y
D:2
Y
×S×Π→Y

其中 
Π
Π 是策略参数空间。

更细地，可分解为评分函数：

score(y∣s,π)
score(y∣s,π)

最终选择为：

y∗=arg⁡max⁡y∈C(s)score(y∣s,π)
y
∗
=arg
y∈C(s)
max
	​

score(y∣s,π)

若是最小化问题，则改为 
arg⁡min⁡
argmin。

Pillar V — Policy System

策略系统定义为控制函数：

P:S×2Y→Π
P:S×2
Y
→Π

即给定状态与候选空间，生成或更新策略参数：

π=P(s,C(s))
π=P(s,C(s))

更一般地，Policy 也可直接对候选空间施加约束：

Pfilter:2Y×S→2Y
P
filter
	​

:2
Y
×S→2
Y

得到过滤后候选：

C′(s)=Pfilter(C(s),s)
C
′
(s)=P
filter
	​

(C(s),s)

## 4. Overall Decision Equation（总决策方程）

综合起来，PDS 的单步决策可写为：

s=S(X)
s=S(X)
C(s)=G(s)
C(s)=G(s)
π=P(s,C(s))
π=P(s,C(s))
y∗=D(C(s),s,π)
y
∗
=D(C(s),s,π)

或者组合成：

y∗=D(G(S(X)), S(X), P(S(X),G(S(X))))
y
∗
=D(G(S(X)),S(X),P(S(X),G(S(X))))

这是 PDS 的最小组合表达。

## 5. Policy-Deformed Decision Space（策略塑形空间）

PDS 的一个关键思想是：Policy 不只是“选最优”，而是塑形候选空间。

定义策略作用后的有效候选集合：

Cπ(s)⊆C(s)
C
π
	​

(s)⊆C(s)

则决策变为：

y∗=arg⁡max⁡y∈Cπ(s)score(y∣s,π)
y
∗
=arg
y∈C
π
	​

(s)
max
	​

score(y∣s,π)

这表达了：

Policy reshapes the feasible future space before decision occurs.

## 6. Dynamic PDS（动态形式）

引入时间 
t
t 后：

st=S(X≤t,Mt)
s
t
	​

=S(X
≤t
	​

,M
t
	​

)
Ct=G(st)
C
t
	​

=G(s
t
	​

)
πt=P(st,Ct,Mt)
π
t
	​

=P(s
t
	​

,C
t
	​

,M
t
	​

)
yt∗=D(Ct,st,πt)
y
t
∗
	​

=D(C
t
	​

,s
t
	​

,π
t
	​

)
Mt+1=U(Mt,st,yt∗,ot)
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
∗
	​

,o
t
	​

)

其中：

Mt
M
t
	​

: memory
ot
o
t
	​

: outcome / feedback
U
U: memory update operator

## 7. Policy Learning（策略学习）

策略更新可以写为：

πt+1=πt+Δ(Et)
π
t+1
	​

=π
t
	​

+Δ(E
t
	​

)

其中 
Et
E
t
	​

 为经验信号，如：

reward
error
risk
CCC coherence
trajectory quality

更一般地：

πt+1=L(πt,Mt,ot)
π
t+1
	​

=L(π
t
	​

,M
t
	​

,o
t
	​

)

其中 
L
L 是策略学习算子。

## 8. Multi-Agent Extension（多智能体形式）

对多个 agent 
i=1,…,N
i=1,…,N，每个 agent 有：

PDSi=(Ki,Si,Gi,Di,Pi)
PDS
i
	​

=(K
i
	​

,S
i
	​

,G
i
	​

,D
i
	​

,P
i
	​

)

共享环境状态 
Et
E
t
	​

，则：

st(i)=Si(Xt(i),Et,Mt(i))
s
t
(i)
	​

=S
i
	​

(X
t
(i)
	​

,E
t
	​

,M
t
(i)
	​

)
yt∗(i)=Di(Gi(st(i)),st(i),πt(i))
y
t
∗(i)
	​

=D
i
	​

(G
i
	​

(s
t
(i)
	​

),s
t
(i)
	​

,π
t
(i)
	​

)

而全局 policy 可定义为：

Πtglobal=Γ(πt(1),…,πt(N),Et)
Π
t
global
	​

=Γ(π
t
(1)
	​

,…,π
t
(N)
	​

,E
t
	​

)

这里 
Γ
Γ 表示协同 / 竞争 / 约束协调算子。

## 9. Final Mathematical Statement（最终数学陈述）

A Policy Decision System is a structured decision process in which state construction, candidate generation, policy deformation, and decision selection are composed into a closed-loop control system over future space.