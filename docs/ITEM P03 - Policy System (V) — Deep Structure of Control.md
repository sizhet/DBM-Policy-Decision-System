# ITEM P03
# Policy System (V) — Deep Structure of Control

（策略系统（V）——控制层的深层结构）

## 1. Why Policy Matters

Traditional systems:

- optimize objective 
- but do not model why the objective exists

PDS introduces:

> **Policy = explicit representation of intent, constraint, and control**

## 2. Policy Decomposition

Policy (V) can be decomposed into four fundamental sub-components:

### 🔷 V = Goal + Constraint + Risk + Strategy
  
    V =
        Goal System
      + Constraint System
      + Risk System
      + Strategy System

## 3. Subsystems Definition
### 3.1 Goal System

Defines:

- what the system wants to achieve

Examples:

- maximize reward
- reach target state
- maintain stability

### 3.2 Constraint System

Defines:

- what is allowed / forbidden

Examples:

- safety rules
- resource limits
- logical constraints

### 3.3 Risk System

Defines:

- acceptable uncertainty

Examples:

- volatility limits
- failure tolerance
- trajectory risk

👉 对应 TRI（Trajectory Risk Intelligence）

### 3.4 Strategy System

Defines:

- how to act under goal + constraint + risk

Examples:

- exploration vs exploitation
- conservative vs aggressive
- adaptive policy switching

## 4. Policy as a Field）

A policy is not merely a parameter, but rather：

> **a shaping field over the candidate space**

#### Modes of Expression：

    Policy(Y) → modifies:
        - candidate distribution
        - scoring function
        - reachable space
        
#### 🔥 Core Insights

> **Policy does not select outcomes directly —**\
>> **it reshapes the space in which selection occurs**

## 5. Policy–Decision Interaction

#### Decision Engine (IV)
- local evaluator

#### Policy (V)
- global controller

#### Relations：
    Decision = local optimum
    Policy = global constraint

#### 🔥 Key Conclusions

> **Decision chooses best option,**\
> **Policy defines what “best” means.**

## 6. DBM-SI Context

A policy can carry：

- CCC preference
- trajectory regime selection
- risk-aware routing
- evolution triggers