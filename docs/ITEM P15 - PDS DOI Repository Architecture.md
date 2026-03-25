# ITEM P15
# PDS DOI Repository Architecture

（PDS DOI 仓库结构设计）

## 1. Design Principles（设计原则）
- 🧭 强导引（reader journey first）
- 🧩 模块化（theory / runtime / visuals 分离）
- 🔁 可演化（支持后续版本扩展）
- 📊 图文并重（diagram-driven understanding）

## 2. Top-Level Structure（顶层结构）
    
    PDS/
    ├── README.md
    ├── START-HERE.md
    ├── LICENSE
    ├── CITATION.cff
    ├── zenodo.json
    │
    ├── docs/
    │   ├── 01-introduction.md
    │   ├── 02-five-pillars.md
    │   ├── 03-fish-control-structure.md
    │   ├── 04-canonical-diagram.md
    │   ├── 05-policy-system.md
    │   ├── 06-dynamic-pds.md
    │   ├── 07-unified-loop.md
    │   ├── 08-formalization.md
    │   ├── 09-benchmarks.md
    │   ├── 10-multi-agent.md
    │   ├── 11-comparisons.md
    │   ├── 12-use-cases.md
    │   └── 99-glossary.md
    │
    ├── assets/
    │   ├── posters/
    │   │   ├── PDS-Canonical-Poster.png
    │   │   ├── PDS-Fish-Control-Diagram.png
    │   │   └── PDS-Trinity-Map.png
    │   │
    │   ├── diagrams/
    │   │   ├── canonical.mmd
    │   │   ├── dpds-loop.mmd
    │   │   └── multi-agent.mmd
    │   │
    │   └── figures/
    │       ├── structure-dynamics-control.png
    │       └── policy-field.png
    │
    ├── runtime/
    │   ├── src/main/java/com/dbm/pds/
    │   ├── src/test/java/com/dbm/pds/
    │   └── README.md
    │
    ├── examples/
    │   ├── finance-demo/
    │   ├── driving-demo/
    │   └── coding-demo/
    │
    ├── benchmarks/
    │   ├── metrics.md
    │   ├── templates/
    │   │   ├── finance.md
    │   │   ├── driving.md
    │   │   └── coding.md
    │   └── results/
    │
    └── manifesto/
        ├── PDS-10-SENTENCE.md
        └── PDS-ONE-PAGE.md
        
## 3. Reader Journey（读者路径设计）

#### 🥇 初学者路径（3–10分钟）
    README → START-HERE → Canonical Poster

#### 🥈 技术读者路径（30–60分钟）
    Five Pillars → Fish-Control → Policy → Dynamic PDS → Use Cases

#### 🥉 研究者路径（深入）
    Formalization → Benchmarks → Multi-Agent → Comparisons

## 4. Key Files（关键文件）

#### README.md
- 第一屏（P14）
- 一句话 + 图 + 核心结构

#### START-HERE.md
- 快速导读
- 章节导航
- 推荐阅读路径

#### docs/
- 主体论文内容
- ITEM P01–P13 拆分

#### src/
- Java skeleton
- minimal working system

#### assets/
- 所有图（PNG + 源文件）
- 支持论文 / 海报 / 演示

## 5. DOI Readiness Checklist（发布核查）
- README 第一屏完成
- START-HERE 存在
- Canonical Poster 已生成
- CITATION.cff 完整
- zenodo.json 完整
- docs/结构完整
- assets/图齐全
- runtime 可运行

## 🔥 核心目标

> 让读者在 3 分钟、30 分钟、3 小时三个层级都能“进入 PDS”。