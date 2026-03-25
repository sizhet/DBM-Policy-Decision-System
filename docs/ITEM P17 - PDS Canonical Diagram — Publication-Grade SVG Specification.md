# ITEM P17
# PDS Canonical Diagram — Publication-Grade SVG Specification

（PDS 标准结构图——出版级 SVG 设计说明）

## 1. Figure Title

**Policy Decision System (PDS) — Canonical Fish-Control Architecture**

副标题可选：

**A Control-Centric Paradigm for Intelligent Systems**

## 2. Figure Purpose（图的用途）

这张图的目标不是展示细节，而是作为 **PDS 的“总图 / 标志图 / 论文主图”**，承担四个任务：

1. 一眼说明 PDS 的五层结构
2. 一眼说明双向流：生成流 + 控制流
3. 一眼说明 Policy 是顶层控制场
4. 一眼连接 CCC / Trajectory / PDS 三位一体

## 3. Overall Layout（总体布局）

建议使用 **纵向海报式布局**，自上而下五层：

Top

     ┌──────────────────────────────┐
     │ Policy Field (V)             │
     └──────────────────────────────┘
                   ↓
     ┌──────────────────────────────┐
     │ Decision Engine (IV)         │
     └──────────────────────────────┘
                   ↓
     ┌──────────────────────────────┐
     │ Future Candidate Space (III) │
     └──────────────────────────────┘
                   ↓
     ┌──────────────────────────────┐
     │ State / Trajectory (II)      │
     └──────────────────────────────┘
                   ↓
     ┌──────────────────────────────┐
     │ Knowledge / CCC (I)          │
     └──────────────────────────────┘
     
Bottom

同时在中央放一条主轴箭头。
左侧放 **Generative Flow**，右侧放 **Control Flow**。

## 4. Core Visual Composition（核心视觉组成）
### 4.1 Central Stack（中央五层堆栈）

五个大模块框建议使用：

- 圆角矩形
- 垂直居中对齐
- 层间等距
- 每层高度一致
- 宽度从下到上可略微收窄，形成“上层控制下层”的轻微金字塔感

建议文案：

#### Layer V

**Policy Field (V)**\
Goal / Constraint / Risk / Strategy

#### Layer IV

**Decision Engine (IV)**\
Scoring / Re-ranking / Optimization

#### Layer III

**Future Candidate Space (III)**\
Trajectory / Action / Plan Candidates

#### Layer II

**State / Trajectory (II)**\
Context / History / IR / Current State

#### Layer I

**Knowledge Model (I)**\
Y=f(X) / CCC / Behavioral CCC

### 4.2 Left-Side Flow（左侧：生成流）

在左侧画一列向下偏中的箭头和标签：

#### Generative Flow

    Knowledge → State → Candidates → Decision

说明：

- 从底层能力和状态出发
- 生成多未来空间
- 再交由 decision 处理

### 4.3 Right-Side Flow（右侧：控制流）

在右侧画一列向上偏中的箭头和标签：

#### Control Flow

    Policy → Decision → Candidate Shaping → State Feedback

说明：

Policy 从顶层下压
决定哪些未来可行
改变选择标准
反馈影响状态与知识更新

### 4.4 Bottom Trinity Footer（底部三位一体横条）

在图底部加一条横向 footer：

    CCC = Structure   |   Trajectory = Dynamics   |   PDS = Control

或者更视觉化一点：

    Structure → Dynamics → Control

这会让图自动接入 DBM-SI 总体系。

## 5. Visual Semantics（视觉语义建议）
### 5.1 Layer Metaphor（层义）

- I：基础结构层
- II：时态上下文层
- III：未来空间层
- IV：评估与选择层
- V：目标与控制场层

### 5.2 Fish-Control Motif（鱼控意象）

不必真的画鱼。建议使用“流场 / 势场”隐喻：

- 在 Policy 层背后加柔和流线
- 流线向下穿透到 Candidate 层
- 表达“不是直接命令结果，而是改变空间形状”

### 5.3 Future Space Motif（未来空间意象）

在 III 层中可放少量分支轨迹线：

- 一条粗主线
- 几条淡分支线
- 表示多候选未来

## 6. Recommended SVG Geometry（建议几何规范）

以 1600 × 2200 画布为例：

- 画布：1600w × 2200h
- 边距：左右各 120，上下各 120
- 中央主框宽：900
- 每层高：220
- 层间距：70
- 圆角半径：28
- 标题区高度：180
- 底部 footer 高度：120

中央五层 Y 坐标可大致为：

- V: y = 260
- IV: y = 550
- III: y = 840
- II: y = 1130
- I: y = 1420

底部 Trinity footer: y = 1820

## 7. Typography（字体层级建议）

Figure Title

大标题，粗体，居中

Policy Decision System (PDS)

副标题略小：

Canonical Fish-Control Architecture

Layer Titles

每层主标题使用较大字号、粗体：

- Policy Field (V)
- Decision Engine (IV)
- Future Candidate Space (III)
- State / Trajectory (II)
- Knowledge Model (I)
- Layer Subtitles

较小字号，简洁列出关键词：

- Goal / Constraint / Risk / Strategy
- Scoring / Re-ranking / Optimization
- Trajectory / Action / Plan Candidates
- Context / History / IR / Current State
- Y=f(X) / CCC / Behavioral CCC
- Side Labels

左右侧标签使用小标题样式：

- Generative Flow
- Control Flow

Footer

底部横条使用中等字号：

    Structure → Dynamics → Control

## 8. Caption（论文图注）

> **Figure P17 — Canonical Fish-Control Architecture of the Policy Decision System (PDS).**\
> The figure presents PDS as a five-layer control-centric architecture composed of Knowledge, State, Future Candidate Space, Decision Engine, and Policy Field. The left side highlights the generative flow through which internal knowledge and current state produce possible futures. The right side highlights the control flow through which policy reshapes candidate space and decision criteria. Intelligence emerges not as one-shot prediction, but as policy-governed traversal over structured future space.

## 9. Short Designer Brief（给设计师的一段话）

> Please design a publication-grade vertical diagram for the Policy Decision System (PDS). The figure should feature five stacked rounded-rectangle layers from bottom to top: Knowledge, State, Future Candidates, Decision Engine, and Policy Field. The left side should show a generative flow; the right side should show a control flow. The overall aesthetic should feel scientific, clean, and structural, emphasizing that policy shapes the future space rather than directly commanding outcomes. The bottom should connect the figure to the Structural Intelligence trinity: CCC = Structure, Trajectory = Dynamics, PDS = Control.