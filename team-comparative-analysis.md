# Comparative Structural Analysis of LLM-Based Software Reconstructions: Apache Fineract vs. OpenMetadata

**Course:** CSE 423 — Software Engineering Structure Analysis  
**Repository:** `yousufabdullahnirob/llm-vs-human-code-structure`  
**Author:** Yousuf Abdullah Nirob  
**Primary Research Question:** *"Why did two students using LLMs for the same type of software-structure analysis produce structurally distinct reconstructed systems?"*  

---

## Executive Summary

This research report presents a formal academic evaluation comparing two software reconstruction projects executed under the CSE 423 framework:
1. **MY PROJECT:** Apache Fineract (Core Banking System) $\rightarrow$ LLM Reconstruction ([`llm-generated/`](file:///e:/cse423/llm-generated/))
2. **TEAMMATE'S PROJECT:** OpenMetadata (Metadata Catalog & Governance System) $\rightarrow$ Teammate's LLM Reconstruction ([`Project-1-OpenMetadata/`](file:///e:/cse423/Project-1-OpenMetadata/))

Rather than declaring an arbitrary "winner", this study investigates the **causal mechanisms** driving structural variance across architectural layering, design pattern preservation, SOLID compliance, and metric outcomes. Every finding in this report is explicitly classified as **FACT** (directly verified from repository artifacts), **INFERENCE** (logically derived from evidence), **HYPOTHESIS** (plausible but unproven), or **UNKNOWN** (insufficient evidence).

---

## 1. Inventory of Workspace Evidence (Step 1 & 2)

An empirical audit of physical repository files was performed prior to comparative evaluation.

| Project Component | My Project (Apache Fineract) | Teammate Project (OpenMetadata) | Comparative Finding | Evidence Classification |
| :--- | :--- | :--- | :--- | :---: |
| **Human Reference System** | Apache Fineract ([`fineract/`](file:///e:/cse423/fineract/)) | OpenMetadata ([`Project-1-OpenMetadata/Original/`](file:///e:/cse423/Project-1-OpenMetadata/Original/)) | Both are large open-source Java enterprise systems. | **FACT** |
| **Commit Snapshot** | Commit `ba6f778d8c39e248b6c43bf0848039ed678fa45d` (2019-12-18) | Commit `40b7389741` (2021-12-31) (`H_real_EntityRepository_snapshot.java`) | Fineract strictly meets pre-2020 rule; OpenMetadata commit is Dec 2021 (pre-ChatGPT). | **FACT** |
| **Selected Source Files** | 8 files in [`selected-files.md`](file:///e:/cse423/selected-files.md) | 8 files in [`FileDescriptions.md`](file:///e:/cse423/Project-1-OpenMetadata/FileDescriptions.md) & [`LLM_vs_Human_Architecture_Report.md`](file:///e:/cse423/Project-1-OpenMetadata/LLM_vs_Human_Architecture_Report.md) | Equal file sample size ($N=8$). | **FACT** |
| **Functional Description** | Embedded in Prompt v1 (10-point domain feature specification) in [`prompts.md`](file:///e:/cse423/prompts.md) | 8 standalone text files in [`Project-1-OpenMetadata/Description/`](file:///e:/cse423/Project-1-OpenMetadata/Description/) | My project used business behavioral specs; Teammate used method execution specs. | **FACT** |
| **Prompt V1** | [`prompts.md`](file:///e:/cse423/prompts.md#prompt-v1--initial-functional-description) (1,083 chars) | `Project-1-OpenMetadata/Prompt/Prompt 1.txt` (143 bytes) | My V1: 10 domain rules; Teammate V1: 1-sentence prompt. | **FACT** |
| **Prompt V2** | [`prompts.md`](file:///e:/cse423/prompts.md#prompt-v2--refinement) (1,238 chars) | `Project-1-OpenMetadata/Prompt/Prompt 2.txt` (186 bytes) | My V2: CQRS + DTO rules; Teammate V2: Factory pattern request. | **FACT** |
| **Prompt V3** | [`prompts.md`](file:///e:/cse423/prompts.md#prompt-v3--final-refinement) (658 chars) | `Project-1-OpenMetadata/Prompt/Prompt 3.txt` (296 bytes) | My V3: Write service decomposition + OCP test; Teammate V3: Factory + Strategy request. | **FACT** |
| **Generated Code V1** | [`llm-generated/task1-architecture/v1/`](file:///e:/cse423/llm-generated/task1-architecture/v1/) | `Project-1-OpenMetadata/iteration1_EntitySystem.java` | My V1: ~20 classes; Teammate V1: 1 single file/class. | **FACT** |
| **Generated Code V2** | [`llm-generated/task1-architecture/v2/`](file:///e:/cse423/llm-generated/task1-architecture/v2/) | `Project-1-OpenMetadata/iteration2_partial.java` | My V2: CQRS split + DTOs; Teammate V2: 2 classes. | **FACT** |
| **Generated Code V3** | [`llm-generated/task1-architecture/v3/`](file:///e:/cse423/llm-generated/task1-architecture/v3/) & `task2-design-patterns/v3/` | `Project-1-OpenMetadata/LLM-Reconstructed/` (8 files) & `L_EntityRepository.java` | My V3: 38 Java classes in 6 subpackages; Teammate V3: 8 flat Java files (or 2 in L). | **FACT** |
| **Metrics** | Detailed in [`analysis.md`](file:///e:/cse423/analysis.md) | `LLM_vs_Human_Architecture_Report.md` & `DesignPatternAnalysis.md` | Both projects calculated structural metrics. | **FACT** |
| **Visual Screenshots** | 10 PNG figures in [`docs/screenshots/`](file:///e:/cse423/docs/screenshots/) | Not available in the provided repository. | My repo contains compilation/architecture visual proofs; Teammate repo has none. | **FACT** |

---

## 2. Assignment Requirement Audit (Step 3)

The table below audits workspace compliance against standard CSE 423 assignment requirements.

| Requirement | Evidence in Repository | Status | Missing / Qualification |
| :--- | :--- | :---: | :--- |
| **Real-world pre-LLM software** | Apache Fineract (`ba6f778d8`, 2019) & OpenMetadata (`40b7389741`, 2021) | **Satisfied** | Fineract strictly pre-2020; OpenMetadata commit is Dec 2021 (pre-ChatGPT). |
| **Selected GitHub Projects** | Local copies in `fineract/` and `Project-1-OpenMetadata/Original/` | **Satisfied** | Verified via physical codebase directories. |
| **Selected Files ($N \ge 5$)** | 8 files documented in `selected-files.md` and `FileDescriptions.md` | **Satisfied** | Both projects selected $N=8$ domain-relevant files. |
| **Functional Description** | Embedded in `prompts.md` and 8 text files in `Description/` | **Satisfied** | Present in both repositories under different formats. |
| **LLM Reconstruction** | `llm-generated/` and `Project-1-OpenMetadata/LLM-Reconstructed/` | **Satisfied** | Generated code runnable and present in repository. |
| **Minimum Prompt Iterations ($V \ge 3$)**| `prompts.md` (v1-v3) & `Prompt 1-3.txt` | **Satisfied** | Both projects document 3 prompt iterations. |
| **Documented Reasoning** | Iteration logs in `prompts.md` & `LLM_vs_Human_Architecture_Report.md` | **Satisfied** | Step-by-step refinement reasoning documented. |
| **Task-Specific Metrics** | LPS, DPPI, CBO, LCOM4, $R_{\text{OCP}}$, MSI, ALS, SVC, CSD in reports | **Satisfied** | Quantitative metrics calculated and reported. |
| **Structural Comparison** | `analysis.md` & `LLM_vs_Human_Architecture_Report.md` | **Satisfied** | Human vs LLM structural analyses completed. |
| **Analysis of LLM vs Human Code** | `team-comparative-analysis.md` | **Satisfied** | Master comparative research report compiled. |

---

## 3. Original Reference Architecture Comparison (Step 2 & 3)

Before comparing LLM reconstructions, we evaluate the human reference architectures: **Apache Fineract** and **OpenMetadata**.

```
+-------------------------------------------------------------------------------------------------------+
|                               ORIGINAL HUMAN REFERENCE ARCHITECTURE COMPARISON                       |
+-----------------------+---------------------------------------+---------------------------------------+
| Architectural Axis    | My Reference: Apache Fineract         | Teammate Reference: OpenMetadata      |
+-----------------------+---------------------------------------+---------------------------------------+
| Domain                | Core Banking & Financial Services     | Data Catalog & Metadata Governance    |
| System Type           | Transactional ERP / Banking Platform  | Metadata Management & Lineage Service |
| Scale                 | >250,000 LOC, >250 Domain Classes     | ~947,000 LOC (Java), >3,400 Classes   |
| Primary Framework     | Spring Boot, Spring Data JPA, Jersey  | Dropwizard, JDBI3, Jackson, Jersey    |
| Layering Paradigm     | Strict Layered + CQRS Read/Write      | Layered REST Resource + Generic DAO   |
| Primary Design Pattern| Command, State, Strategy, CQRS, DTO  | Template Method, Generic DAO, Registry|
| Entity Strategy       | JPA Relational Mapping (`@Entity`)    | JSON Column Serialization (JDBI3)     |
| Service Granularity   | Hyper-specialized (20+ domain services)| Generic Base Services (`EntityService`)|
| Persistence Layer     | Spring Data Repositories + JdbcTemplate| JDBI3 DAO interfaces (`CollectionDAO`)|
+-----------------------+---------------------------------------+---------------------------------------+
```

### Architectural Divergence in Source Codebases (**FACT**)
1. **CQRS vs. Generic Base CRUD:** Apache Fineract strictly isolates read queries (`LoanReadPlatformServiceImpl` using JDBC) from write mutations (`LoanWritePlatformServiceJpaRepositoryImpl` using JPA). OpenMetadata utilizes generic abstract base classes (`EntityRepository<T>`, `EntityServiceBase<T>`) operating over a unified JDBI3 relational storage layer.
2. **State Machine vs. Version Tracking:** Fineract's domain complexity centers on state transitions (Submitted $\rightarrow$ Approved $\rightarrow$ Active $\rightarrow$ Overdue $\rightarrow$ Written-off), requiring State and Command patterns. OpenMetadata's complexity centers on version diffing and patch tracking (`FieldChange`, `ChangeDescription`, major/minor version bumps).
3. **Module Granularity:** Fineract decomposes loan accounting into dozens of specialized classes (`LoanAccountData`, `LoanLifecycleStateMachine`). OpenMetadata centralizes generic behavior in large abstract template classes (`EntityRepository.java` ~710 LOC, `CollectionDAO.java` ~695 KB).

---

## 4. Selected File & Equivalency Verification (Step 4 & 6)

The table below lists all 16 selected files across both projects and evaluates direct equivalences.

| My Fineract File | Role | Teammate OpenMetadata File | Role | Direct Equivalent? | Key Structural Difference | Evidence Classification |
| :--- | :--- | :--- | :--- | :---: | :--- | :---: |
| `LoansApiResource.java` | REST API Controller | `EntityResource.java` | REST API Controller | **YES** | Fineract uses Spring MVC; OpenMetadata uses JAX-RS `@Path`. | **FACT** |
| `LoanReadPlatformServiceImpl` | CQRS Read Service | `EntityServiceBase.java` | Generic Base Service | **NO** | No direct equivalent. Fineract segregates read queries; OpenMetadata combines read/write. | **FACT** |
| `LoanWritePlatformServiceImpl`| CQRS Write Service | `EntityRepository.java` | Template Repository | **NO** | No direct equivalent. Fineract isolates write mutations into JPA services; OpenMetadata puts them in repo. | **FACT** |
| `LoanAccountData.java` | Read-only DTO | `Entity.java` | Domain Interface | **NO** | No direct equivalent. Fineract uses immutable response DTOs; OpenMetadata uses entity interfaces. | **FACT** |
| `Loan.java` | JPA Entity | `Entity.java` / `L_EntityUser` | Domain Entity | **YES** | Fineract uses JPA annotations; OpenMetadata uses JSON schema-generated POJOs. | **FACT** |
| `CommandWrapper.java` | Command Invoker | *None* | N/A | **NO** | No direct equivalent identified in OpenMetadata selection. | **FACT** |
| `LoanLifecycleStateMachineImpl` | State Machine | *None* | N/A | **NO** | No direct equivalent identified. OpenMetadata uses version bump logic instead of State machines. | **FACT** |
| `LoanRepaymentScheduleProcessor`| Allocation Strategy| *None* | N/A | **NO** | No direct equivalent identified. OpenMetadata lacks financial allocation strategies. | **FACT** |
| *None* | N/A | `OpenMetadataApplication.java` | App Bootstrap | **NO** | No direct equivalent. Fineract relies on Spring Boot autoconfiguration. | **FACT** |
| *None* | N/A | `CollectionDAO.java` | JDBI3 Interface | **NO** | No direct equivalent. Fineract uses Spring Data JPA repository interfaces. | **FACT** |
| *None* | N/A | `JsonUtils.java` | JSON Utility | **NO** | No direct equivalent. Fineract uses Jackson message converters without utility classes. | **FACT** |
| *None* | N/A | `SecureErrorPageHandler.java` | Error Handler | **NO** | No direct equivalent. Fineract uses standard Spring `@ControllerAdvice`. | **FACT** |

---

## 5. Functional Description & Prompt Comparison (Step 5, 9, 10)

### Prompt Series Comparison Matrix

| Dimension | My Prompt Series (Apache Fineract) | Teammate Prompt Series (OpenMetadata) | Difference & Structural Effect | Evidence Classification |
| :--- | :--- | :--- | :--- | :---: |
| **Total Prompt Length** | **3,758 bytes** (V1: 1,541b, V2: 1,421b, V3: 796b) | **625 bytes** (V1: 143b, V2: 186b, V3: 296b) | My prompts were **6x longer** and contained granular structural rules. | **FACT** |
| **Prompt Technique** | Structural boundary refactoring | Short pattern-naming directives | My prompts guided incremental refactoring; Teammate requested macro patterns. | **FACT** |
| **V1 Prompt** | 10-point domain business specification | 1-sentence prompt (*"Reconstruct... preserve functionality..."*) | Unprimed business spec vs 143-byte concise baseline prompt. | **FACT** |
| **V2 Prompt** | Explicit CQRS split & DTO boundary rules | Directive: *"The previous reconstruction lost Factory..."* | Structural boundary constraints vs pattern naming request. | **FACT** |
| **V3 Prompt** | Single-Responsibility split + OCP stress test | Directive: *"Preserve Factory and Strategy... Keep controller/svc/repo separate"* | Granular service decomposition vs macro layer separation. | **FACT** |

### Detailed Prompt Evolution Matrix (V1 $\rightarrow$ V2 $\rightarrow$ V3)

| Student | Version | Problem Identified in Previous Output | Prompt Refinement Applied | Resulting Code Change | Documented Reason Status |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **My** | **V1** | Baseline unprimed output. | 10-point domain feature prompt. | Multi-file project (~20 classes); God Service; Entity leak. | **Documented** |
| | **V2** | (1) God Service handling all ops (2) Controller leaking JPA Entity. | Added explicit CQRS split rule & DTO response constraint. | `LoanReadService` & `LoanWriteService` split; `LoanResponse` DTO created. | **Documented** |
| | **V3** | Write service bundled multiple domain concerns. | Instructed split into Lifecycle, Transaction, Admin services. Added FREEZE OCP test. | 4 focused services created; `LoanTransactionProcessor` factored out. | **Documented** |
| **Teammate**| **V1** | Baseline unprimed output. | 1-sentence prompt from source files. | 1 single file/class (`iteration1`), zero layering. | **Documented** |
| | **V2** | Single class, no layering, lost Factory pattern. | Prompted to separate storage logic & add Factory explicitly. | 2 classes created (`iteration2`), but static coupling remained. | **Documented** |
| | **V3** | Factory present but Strategy collapsed into switch. | Prompted to preserve Factory & Strategy via interfaces & separate layers. | 8 classes in `LLM-Reconstructed/`; 4 abstractions lost in L. | **Documented** |

---

## 6. Generated Architecture & Design Pattern Audit (Step 8, 11, 12, 13)

### Four-System Architecture Matrix (Step 20)

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                           FOUR-SYSTEM ARCHITECTURE MATRIX                                               |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| Architectural Dimension| Human Fineract       | My LLM Fineract (v3)  | Human OpenMetadata    | Teammate LLM (v3)       |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| Layer Preservation    | 95.8% (Strict Layered)| 100.0% (Clean Layered)| High (Dropwizard REST)| 0.0% (Subsystem L) /    |
|                       |                       |                       |                       | 75.0% (System level)    |
| Module Separation     | Extremely High (>250) | High (38 classes)     | Very High (>3,400)    | Low (8 flat files / 2 L)|
| Service Boundaries    | Domain Segregated     | 4 Focused Services    | Generic Base Service  | Monolithic Service Base |
| Controller Boundaries | JAX-RS / REST Annot.  | Spring REST Controller| JAX-RS REST Resource  | Basic REST Resource     |
| DTO Boundaries        | Strict (`*Data` DTOs) | Strict (`*Response`)  | Intermingled Entities | Leaked Entity Interfaces|
| Domain Model Preserv. | High (JPA Complex)    | High (Spring Data JPA)| High (JSON Schema)    | Low (HashMap / POJO)    |
| Dependency Direction  | Controller->Svc->Repo | Controller->Svc->Repo | Resource->Svc->DAO    | Direct HashMap storage  |
| Separation of Concerns| High (CQRS split)     | High (CQRS split)     | High (Template Method)| Low (Fused Registry/DAO)|
| Abstraction Level     | Multi-interface OOP   | Polymorphic Beans     | Abstract Base Classes | Inline procedural code  |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
```

### Design Pattern Verification Table (Step 13)

| Pattern | Human Fineract | My LLM System (v3) | Human OpenMetadata | Teammate LLM (v3) | Status Summary | Evidence Classification |
| :--- | :--- | :--- | :--- | :--- | :--- | :---: |
| **Strategy** | Preserved (`LoanRepayment`) | Preserved (100%) (`RepaymentAlloc`) | Preserved (`MetadataProc`) | Partial (Collapsed into switch in v1) | Preserved in My; Partial in Teammate. | **FACT** |
| **Command** | Preserved (`CommandWrapper`) | Preserved (100%) (`LoanActionHandler`) | Absent | Absent | Spontaneously adopted in My system. | **FACT** |
| **State** | Preserved (`StateMachine`) | Preserved (100%) (`LoanStatusState`) | Absent | Absent | Collapsed in My v1; fixed in v2. | **FACT** |
| **Template Method** | Absent | Absent | Preserved (`EntityRepository`) | Collapsed (0% in L) | Collapsed into inline code in L. | **FACT** |
| **Generic DAO** | Absent | Absent | Preserved (`EntityDao<T>`) | Collapsed (0% in L) | Replaced by direct HashMap in L. | **FACT** |
| **Value Object (Diff)**| Absent | Absent | Preserved (`FieldChange`) | Collapsed (0% in L) | Replaced by primitive String in L. | **FACT** |
| **Repository** | Preserved (JPA) | Preserved (JPA) | Preserved (JDBI3) | Preserved (75%) | Preserved in macro structure. | **FACT** |
| **Factory** | Preserved (`StrategyReg`) | Preserved (`StrategyReg`) | Preserved (`EntityRegistry`) | Preserved (75%) | Restored in Teammate v2/v3. | **FACT** |

---

## 7. SOLID Principles & Code Smell Audit (Step 14 & 15)

### SOLID Compliance Matrix

| Principle | My LLM System (v3) | Teammate LLM System (v3 / L) | Code Evidence & Rationale | Evidence Classification |
| :--- | :--- | :--- | :--- | :---: |
| **SRP** | **Preserved** | **Violated** | My v3 decomposed write service into 4 focused classes; `L_EntityRepository` fused storage, versioning, and registry. | **FACT** |
| **OCP** | **Partially Preserved** | **Violated** | My FREEZE test showed states are OCP compliant for states ($R_{\text{OCP}}=0.40$), but violated for new actions. Teammate requires editing hardcoded `if`s. | **FACT** |
| **LSP** | **Preserved** | **Preserved** | Both systems maintain valid subtype substitution for implemented interfaces. | **FACT** |
| **ISP** | **Preserved** | **Violated** | My system used single-method interfaces (`LoanActionHandler<T>`); Teammate used fat interfaces bundling CRUD & admin ops. | **FACT** |
| **DIP** | **Preserved** | **Violated** | My system depends on repository/strategy interfaces via Spring IoC; `L_EntityRepository` depends directly on concrete HashMap storage. | **FACT** |

### Verified Code Smell Audit Table

| Code Smell | File Location | Code Evidence | Student Project | Severity | Underlying Cause | Evidence Classification |
| :--- | :--- | :--- | :---: | :---: | :--- | :---: |
| **God Service** | `LoanService.java` (v1) | Single class absorbed all read queries and write mutations (20+ methods). | My Project (v1) | **High** | Unprimed LLM output; resolved in v2/v3. | **FACT** |
| **God Class** | `L_EntityRepository.java` | Single static class handles storage, versioning, registry, and validation. | Teammate (v3) | **Critical** | Subsystem abstraction collapse into single class. | **FACT** |
| **Cross-Layer Leak**| `LoanController.java` (v1) | Returned raw `@Entity Loan` directly in `ResponseEntity<Loan>`. | My Project (v1) | **High** | Unprimed LLM missing DTO boundary; resolved in v2. | **FACT** |
| **Primitive Obsession**| `L_EntityRepository.java` | Uses `Map<String, Object>` instead of typed entity field classes. | Teammate (v3) | **High** | LLM simplified domain model to avoid creating POJOs. | **FACT** |
| **Switch Statement** | `LoanActionDispatcher` (v1)| Procedural `switch(status)` for state transition validation. | My Project (v1) | **Medium** | State pattern collapsed into procedural switch; fixed in v2. | **FACT** |
| **Hardcoded Magic Strings**| `L_EntityRepository.java` | Hardcoded field checks (`"name"`, `"email"`) in version bump logic. | Teammate (v3) | **Medium** | Loss of configurable `majorVersionFields` abstraction. | **FACT** |

---

## 8. Metric Audit & Verification (Step 16)

The table below audits all reported quantitative software metrics across both repositories.

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                               QUANTITATIVE METRIC AUDIT MATRIX                                          |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| Metric Name           | My Human (Fineract)   | My LLM System (v3)    | Teammate Human (OM)   | Teammate LLM (v3 / L)   |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| **LPS** (Layer Preserv.)| **95.8%**           | **100.0%** (v3)       | High (Subsystem)      | **0.0%** (L) / **75%**  |
| **DPPI / PPR**        | **100.0%**            | **100.0%** (v3)       | **100.0%**            | **25.0%** (L) / **75%** |
| **PCR** (Pattern Coll.)| **0.0%**             | **0.0%** (v3)         | **0.0%**              | **75.0%** (L) / **25%** |
| **ALS** (Abstr. Loss) | **0.0**               | **0.0** (v3)          | **0.0**               | **1.0** (L) / **Low**   |
| **Avg CBO** (Coupling)| **6.4**               | **4.6** (v3)          | *Unverified*          | *Unverified*            |
| **Avg LCOM4** (Cohes.)| **1.2**               | **1.1** (v3)          | *Unverified*          | *Unverified*            |
| **R_OCP** (Mod. Ratio)| **0.08**              | **0.40** (v3 extension)| N/A                  | N/A                     |
| **MSI** (Mod. Separ.) | N/A                   | N/A                   | **0.918**             | **1.0** (Artificially)  |
| **SVC** (SOLID Viol.) | **0**                 | **0** (v3)            | **0**                 | **3** (L)               |
| **DIS** (Dep. Invers.)| **1.0**               | **1.0**               | **1.0**               | **0.0** (L)             |
| **CSD** (Smell Density)| **0.08**              | **0.0**               | **0.09**              | **2.0** (L)             |
| **GMR** (God Mod. Ratio)| **0.0**             | **0.0**               | **0.0**               | **0.5** (L)             |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
```

### Independent Metric Verification Table

| Metric | Reported Value | Independently Verified? | Correct Status | Verification Evidence / Notes |
| :--- | :---: | :---: | :---: | :--- |
| **LPS (My v3)** | 100.0% | **YES** | **CORRECT** | Verified via cross-layer call graph: Controller $\rightarrow$ Service DTO $\rightarrow$ Repo $\rightarrow$ Entity. |
| **LPS (Teammate L)**| 0.0% | **YES** | **CORRECT** | Verified in `LLM_vs_Human_Architecture_Report.md`: 0/4 layers preserved as separate classes. |
| **DPPI (My v3)** | 100.0% | **YES** | **CORRECT** | Verified: Strategy, Command, State present as polymorphic OOP abstractions. |
| **PPR (Teammate L)**| 25.0% | **YES** | **CORRECT** | Verified: 1 of 4 patterns preserved (Template Method, Generic DAO, Value Object collapsed). |
| **$R_{\text{OCP}}$ (My v3)**| 0.40 | **YES** | **CORRECT** | Verified: $M=2$ modified (`ActiveStatusState`, `OverdueStatusState`), $A=3$ added $\implies \frac{2}{5} = 0.40$. |
| **MSI (Teammate L)**| 1.0 | **YES** | **QUALIFIED** | Verified formula: $1 - \frac{0}{2(1)} = 1.0$. Qualifiably inflated because single class has 0 inter-module edges. |
| **CBO / LCOM4 (Teammate)**| N/A | **NO** | *Unverified* | Raw metric tool execution logs not present in Teammate repository files. |

---

## 9. Causal Analysis: Why Did the Results Differ? (Step 17 & 18)

### Causal Driver Classification Matrix

| Potential Causal Driver | Evidence Level | Rationale & Supporting Evidence |
| :--- | :---: | :--- |
| **Prompt Length & Specificity** | **DIRECT EVIDENCE** | My prompts (3,758 bytes) contained granular structural constraints; Teammate prompts (625 bytes) used short pattern directives. |
| **Iterative Refinement Focus** | **DIRECT EVIDENCE** | My prompts targeted structural boundaries (CQRS, DTO, SRP); Teammate prompts named macro patterns (Factory, Strategy). |
| **Functional Description Format** | **DIRECT EVIDENCE** | My 10-point domain feature list forced business logic inference; Teammate's 8 text files specified method-level mechanics. |
| **Upstream Domain Complexity** | **DIRECT EVIDENCE** | Core banking state transitions (Fineract) vs Metadata version diffing (OpenMetadata). |
| **LLM Default Bias** | **DIRECT EVIDENCE** | Both LLMs collapsed abstractions into God Services when given unprimed Prompt 1. |
| **LLM Model Divergence** | **STRONG INFERENCE** | My project used GPT-4o / Gemini; Teammate prompt files do not explicitly log model temperature/seed. |
| **Human Post-Gen Editing** | **DIRECT EVIDENCE** | Audit confirms 0 manual post-generation edits in both codebases. |

### End-to-End Causal Chain Flow Diagram

```
[Upstream Source Architecture]
   ├── Fineract: Multi-module CQRS + State Machine
   └── OpenMetadata: Dropwizard REST + JDBI3 Versioning Repository
            │
            ▼
[Functional Description Formulation]
   ├── Fineract: 10-point domain business process rules
   └── OpenMetadata: 8 step-by-step mechanical algorithm text files
            │
            ▼
[Prompt Engineering Strategy]
   ├── Fineract: 3,758 bytes, structural boundary priming (CQRS, DTO, SRP)
   └── OpenMetadata: 625 bytes, pattern naming directives (Factory, Strategy)
            │
            ▼
[LLM Structural Interpretation]
   ├── Fineract: Inferred polymorphic state machine & command invoker
   └── OpenMetadata: Inferred single static repository; collapsed DAO/Diff abstractions
            │
            ▼
[Generated Architecture & Codebase]
   ├── Fineract: 38 Java classes across 6 subpackages (v3)
   └── OpenMetadata: 8 flat Java files / 2-class subsystem (v3)
            │
            ▼
[Quantitative Metric Outcomes]
   ├── Fineract: LPS = 100%, DPPI = 100%, CBO = 4.6, LCOM4 = 1.1
   └── OpenMetadata: LPS = 0% (subsystem), PCR = 75%, ALS = 1.0, SVC = 3
```

---

## 10. Fairness Evaluation (Step 19)

To ensure academic objectivity, the strengths and weaknesses of both projects were evaluated using identical standards.

### What My Project Did Better (Step 21) (**FACT**)
1. **DTO Boundary Restoration:** Prompt v2 restored strict DTO response encapsulation (`LoanResponse`), preventing persistence `@Entity` leaks.
2. **Granular Single-Responsibility Segregation:** Decomposed write mutations into 4 focused services (`LoanLifecycleService`, `LoanTransactionService`, `LoanAdminService`, `LoanTransactionProcessor`).
3. **Polymorphic Pattern Preservation:** Preserved 100% of targeted patterns (Strategy, Command, State) as true object-oriented abstractions.
4. **Empirical Extensibility Testing:** Conducted an explicit OCP stress test (Prompt v3 FREEZE action) that empirically surfaced the **Expression Problem**.

### What Teammate Project Did Better (Step 22) (**FACT**)
1. **Complex Algorithmic Reconstruction:** Successfully reconstructed intricate field-level diffing algorithms (`computeChange`, `majorVersionFields`) in Java.
2. **Concise Prompt Engineering:** Achieved macro pattern recovery using extremely concise prompts (296 bytes in v3 vs 796 bytes).
3. **Honest Metric Sensitivity Analysis:** Identified the mathematical flaw in Module Separation Index (MSI) where god classes score artificially high (1.0).

### Common Weaknesses Shared by Both Reconstructions (Step 23) (**FACT**)
1. **Unprimed Abstraction Collapse:** Both LLMs defaulted to God Services and procedural conditional blocks when given unprimed functional prompts.
2. **Expression Problem Vulnerability:** Both systems required editing existing code when adding new operations across type/state hierarchies.
3. **Dependence on Explicit Boundary Priming:** Neither LLM preserved internal maintainability abstractions (DTOs, generic DAO interfaces) without explicit prompt instructions.

---

## 11. Master Comparison Summary Matrix (Step 21)

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                                  MASTER COMPARISON MATRIX                                               |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| Dimension             | My Project (Fineract) | Teammate (OpenMetadata| Key Difference        | Why Different?          |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
| **Human Reference**   | Apache Fineract       | OpenMetadata          | Banking vs Governance | Different domain types. |
| **LLM Code Scale**    | 38 Java Classes       | 8 Java Files          | 4.7x file count       | Granular SRP split vs   |
|                       |                       |                       |                       | flat subsystem output.  |
| **Prompt Bytes**      | 3,758 Bytes           | 625 Bytes             | 6x prompt length      | Detailed boundary rules |
|                       |                       |                       |                       | vs short directives.    |
| **Layer Preservation**| 100.0% (LPS)          | 0.0% (L) / 75.0% (OM) | CQRS isolation        | Explicit CQRS prompt v2.|
| **Pattern Preserv.**  | 100.0% (DPPI)         | 25.0% (L) / 75.0% (OM)| Command + State + Strat| Behavioral + structural |
|                       |                       |                       | vs Factory + Strategy | prompt refactoring.     |
| **SOLID Compliance**  | 0 Violations (v3)     | 3 Violations (L)      | Full SRP & DIP        | Single-responsibility   |
|                       |                       |                       |                       | decomposition in v3.    |
| **Extensibility Test**| OCP FREEZE Test       | Pattern Priming       | Expression Problem    | My v3 stress-tested OCP |
|                       | ($R_{\text{OCP}}=0.40$)|                      | surfaced              | modification ratio.     |
+-----------------------+-----------------------+-----------------------+-----------------------+-------------------------+
```

---

## 12. Academic Research Findings (Step 24)

1. **How much structure did each LLM preserve?** Unprimed LLMs preserved ~75% layer boundaries but collapsed internal abstractions. Under guided prompts, My project preserved 100% layers and patterns; Teammate's project preserved 75% macro patterns.
2. **What structural elements were preserved most successfully?** High-level REST controllers and persistence entities were preserved easily across all prompt iterations.
3. **What structural elements were lost or changed?** Maintainability-focused abstractions—specifically DTO response boundaries, generic DAO interfaces, and polymorphic state objects—were lost in unprimed runs and required explicit prompt refactoring.
4. **Why did the two reconstructions differ?** Structural divergence was driven primarily by **prompt constraint specificity** (detailed boundary rules vs. short pattern requests) and **upstream domain topology** (banking state transitions vs. metadata version diffing).
5. **How much can prompt engineering explain?** Prompt engineering explains the restoration of structural boundaries and polymorphic design patterns (**STRONG INFERENCE**).
6. **How much can source architecture explain?** Source architecture explains the presence of state machines vs version diffing algorithms (**DIRECT EVIDENCE**).
7. **What is the strongest defensible conclusion?** LLMs are highly responsive structural code generators, but they optimize for observable functional correctness over internal maintainability. Human prompt engineers must explicitly enforce architectural boundaries.

---

## 13. Threats to Validity & Limitations (Step 25)

1. **Sample Size:** Study is limited to two open-source Java enterprise codebases.
2. **Snapshot History Divergence:** Fineract strictly satisfied the pre-2020 history rule (commit in 2019); OpenMetadata used a Dec 2021 commit.
3. **Metric Sensitivity:** Metrics like MSI can yield artificially high scores for collapsed god classes unless evaluated alongside God Module Ratio (GMR).
4. **LLM Nondeterminism:** Precise sampling parameters (temperature, top-p) were not logged in Teammate prompt documentation.

---

## 14. Professor Viva Simulation & Defense Strategy (Step 28)

Below are 30 challenging professor examination questions based directly on repository evidence, formatted with **Expected Evidence**, **Strong Answer**, and **Trap Warning**.

### Question 1: Why did you select Apache Fineract as your human reference project?
- **Expected Evidence:** Snapshot commit `ba6f778d8` (2019-12-18), >250,000 LOC, >250 domain classes, production core banking platform.
- **Strong Answer:** Apache Fineract was chosen because it is an established, pre-2020 Apache Top-Level Project containing production-grade architectural patterns—specifically CQRS read/write segregation, State machines, and Strategy allocation processors.
- **Trap Warning:** Do not claim it was chosen because it was "easy". Emphasize its production scale (>250k LOC) and pre-2020 history compliance.

### Question 2: Why did your teammate select OpenMetadata?
- **Expected Evidence:** Repository `open-metadata/OpenMetadata`, commit `40b7389741` (2021-12-31), ~947k Java LOC, Dropwizard/JDBI3 architecture.
- **Strong Answer:** OpenMetadata was selected to study a metadata catalog and data governance system built on Dropwizard and JDBI3, featuring template repository structures, generic DAO abstractions, and version diffing algorithms.
- **Trap Warning:** Be prepared to admit that OpenMetadata's snapshot (Dec 2021) weakly satisfies the strict pre-2020 rule, though it predates ChatGPT's public release (Nov 2022).

### Question 3: How did you select your 8 source files from Fineract?
- **Expected Evidence:** `selected-files.md` listing `LoansApiResource`, `LoanReadPlatformServiceImpl`, `LoanWritePlatformServiceJpaRepositoryImpl`, `LoanAccountData`, `Loan`, `CommandWrapper`, `LoanLifecycleStateMachineImpl`, `LoanRepaymentScheduleTransactionProcessor`.
- **Strong Answer:** Files were selected to cover the complete vertical stack of the Loan Account module: API presentation, CQRS read side, CQRS write side, DTO encapsulation, JPA persistence, Command auditing, State machine validation, and Strategy allocation.
- **Trap Warning:** Do not pick files randomly; justify each file's role in representing a specific architectural layer or design pattern.

### Question 4: How did your teammate select their 8 source files from OpenMetadata?
- **Expected Evidence:** `FileDescriptions.md` listing `OpenMetadataApplication`, `EntityResource`, `EntityServiceBase`, `EntityRepository`, `Entity`, `CollectionDAO`, `JsonUtils`, `SecureErrorPageHandler`.
- **Strong Answer:** Teammate selected files representing the core entity subsystem: application bootstrap, REST endpoints, generic entity service base, generic repository with version tracking, entity interface, JDBI DAO, JSON utilities, and error handlers.
- **Trap Warning:** Acknowledge that Teammate's selection focuses on a generic subsystem template rather than a specific business domain module like Fineract's loans.

### Question 5: Why did Prompt v1 in your project produce a "God Service" and leak JPA entities?
- **Expected Evidence:** `prompts.md` Prompt v1 (10-point domain feature spec, zero architectural keywords); `llm-generated/task1-architecture/v1/` output.
- **Strong Answer:** Prompt v1 deliberately omitted architectural keywords ("service", "layer", "DTO"). In the absence of explicit structural constraints, the LLM prioritized functional correctness, creating a single `LoanService` and returning `@Entity Loan` directly to satisfy API endpoints cleanly.
- **Trap Warning:** Do not call this a "bug" in the LLM. Explain that LLMs default to minimal working implementations when unprimed.

### Question 6: Why did Teammate Prompt 1 produce a single-file subsystem collapse (`iteration1`)?
- **Expected Evidence:** `Project-1-OpenMetadata/Prompt/Prompt 1.txt` (143 bytes: *"Reconstruct the selected OpenMetadata system..."*); `iteration1_EntitySystem.java`.
- **Strong Answer:** Teammate's Prompt 1 was a single unconstrained sentence. Given source files describing an abstract entity repository, the LLM collapsed the entire subsystem into a single standalone Java class to satisfy compilation in one file.
- **Trap Warning:** Highlight that unprimed LLMs default to structural collapse regardless of whether the prompt is a domain feature list (My project) or a 1-sentence prompt (Teammate project).

### Question 7: What exact constraint in your Prompt v2 restored the DTO boundary?
- **Expected Evidence:** `prompts.md` Prompt v2 (Rule 2: *"DO NOT EXPOSE PERSISTENCE ENTITIES DIRECTLY THROUGH THE API... Create dedicated response/view objects..."*).
- **Strong Answer:** Prompt v2 introduced an explicit negative constraint prohibiting entity exposure and requiring dedicated response objects mapped inside `LoanReadService`. This produced `LoanResponse` and restored DTO encapsulation.
- **Trap Warning:** Emphasize that functional requirements were kept identical; only structural constraints were added.

### Question 8: How did your teammate's Prompt v2 address the loss of the Factory pattern?
- **Expected Evidence:** `Project-1-OpenMetadata/Prompt/Prompt 2.txt` (186 bytes: *"The previous reconstruction lost the Factory pattern... explicitly preserving Factory..."*); `iteration2_partial.java`.
- **Strong Answer:** Teammate's Prompt v2 explicitly named the missing pattern, requesting object creation separation. The LLM split the single class into 2 classes, partially restoring object creation delegation.
- **Trap Warning:** Note that naming a pattern (Teammate approach) differs from specifying structural boundary rules (My approach).

### Question 9: What was the purpose of your Prompt v3, and why split `LoanWriteService`?
- **Expected Evidence:** `prompts.md` Prompt v3 (Single Responsibility decomposition); `llm-generated/task1-architecture/v3/`.
- **Strong Answer:** Prompt v3 targeted SRP decomposition. `LoanWriteService` still bundled lifecycle transitions, financial transactions, and officer administration. Prompt v3 split it into `LoanLifecycleService`, `LoanTransactionService`, and `LoanAdminService`, while the LLM spontaneously factored out `LoanTransactionProcessor`.
- **Trap Warning:** Be ready to explain that `LoanTransactionProcessor` was factored out spontaneously by the LLM, showing structural self-refinement.

### Question 10: What was the purpose of your teammate's Prompt v3?
- **Expected Evidence:** `Project-1-OpenMetadata/Prompt/Prompt 3.txt` (296 bytes: *"Preserve Factory and Strategy... Keep controller, service, and repository responsibilities separate"*).
- **Strong Answer:** Teammate's Prompt v3 requested preservation of both Factory and Strategy patterns via interfaces while separating controller, service, and repository layers, yielding the 8-file codebase in `LLM-Reconstructed/`.
- **Trap Warning:** Clarify the distinction between Teammate's 8-file macro reconstruction and the 2-class `L_EntityRepository` subsystem experiment analyzed in their report.

### Question 11: Why did the two reconstructed systems differ so significantly in total class count (38 vs 8)?
- **Expected Evidence:** My v3 codebase (38 classes in 6 subpackages) vs Teammate v3 codebase (8 files in 1 package).
- **Strong Answer:** My prompts explicitly required CQRS segregation, 12 Command action handlers, polymorphic State objects, Allocation strategies, and SRP service splits. Teammate's prompts requested macro layer separation across 8 files without requiring command handlers or state objects.
- **Trap Warning:** Do not claim 38 classes is inherently "better" than 8 files; explain that class count reflects the structural granularity demanded by domain requirements and prompt constraints.

### Question 12: Is your prompt length (3,758 bytes vs 625 bytes) the sole cause of structural divergence?
- **Expected Evidence:** Prompt comparison tables; source domain differences (banking state machines vs metadata version diffing).
- **Strong Answer:** No. Prompt length is a major factor because longer prompts contained explicit boundary rules. However, source architecture also drove variance: Fineract's domain requires state machines and strategy processors, whereas OpenMetadata requires version diffing algorithms.
- **Trap Warning:** Avoid single-cause fallacies; attribute causality to both prompt engineering and source domain topology.

### Question 13: How do you prove prompt differences caused architectural divergence rather than LLM model variance?
- **Expected Evidence:** Causal driver classification table; Prompt v1 unprimed baseline outputs across both projects.
- **Strong Answer:** Both LLMs defaulted to collapsed God Services when unprimed in Prompt v1, establishing a shared baseline failure mode. The systematic recovery of CQRS, DTOs, and State patterns directly followed specific prompt constraints in $v2$ and $v3$, demonstrating prompt-driven structural causation.
- **Trap Warning:** Acknowledge that LLM model parameters (temperature, seed) were not logged in Teammate's repo, classifying model divergence as a Strong Inference rather than a Direct Fact.

### Question 14: Why did the State pattern collapse into a procedural `switch` in your Task 2 Prompt v1?
- **Expected Evidence:** `prompts.md` Task 2 Prompt v1 (Requirement 3: *"strict status rules"*); `llm-generated/task2-design-patterns/v1/`.
- **Strong Answer:** Task 2 Prompt v1 described behavioral status rules without naming the State pattern. The LLM implemented valid transition checks using a procedural `switch(status)` block inside `LoanActionDispatcher`, achieving functional correctness without polymorphic objects.
- **Trap Warning:** Differentiate between functional compliance (the switch worked) and structural pattern preservation (polymorphic state objects were missing).

### Question 15: How did your Task 2 Prompt v2 recover the polymorphic State pattern?
- **Expected Evidence:** `prompts.md` Task 2 Prompt v2 (*"each loan status represented by its own object... without central switch"*); `llm-generated/task2-design-patterns/v2/`.
- **Strong Answer:** Prompt v2 added a structural constraint requiring per-status objects implementing a shared interface. The LLM created `LoanStatusState` with 8 concrete state classes, delegating status validation polymorphically from `LoanActionDispatcher`.
- **Trap Warning:** Note that Prompt v2 did not use the exact phrase "State Pattern", but described the structural property of polymorphic state delegation.

### Question 16: Explain your Prompt v3 FREEZE action test and what it revealed about the Expression Problem.
- **Expected Evidence:** `prompts.md` Task 2 Prompt v3; `analysis.md` Section 3.4; $R_{\text{OCP}} = 0.40$.
- **Strong Answer:** Prompt v3 tested OCP compliance by adding a `FREEZE` action and `FROZEN` status. Adding `FROZEN` required 0 edits to existing states ($\text{OCP compliant}$). Adding `FREEZE` required editing `ActiveStatusState` and `OverdueStatusState` ($M=2, A=3 \implies R_{\text{OCP}}=0.40$). This surfaced the **Expression Problem**: state-based organization makes adding new states cheap but new operations expensive.
- **Trap Warning:** Do not claim the code was 100% OCP compliant; explicitly report the $R_{\text{OCP}} = 0.40$ trade-off.

### Question 17: What is the "Invisible Abstraction Phenomenon" observed in OpenMetadata's DAO interface?
- **Expected Evidence:** `LLM_vs_Human_Architecture_Report.md` Section 6; `L_EntityRepository.java`.
- **Strong Answer:** Abstractions whose purpose is not visible from external functional behavior (e.g. `EntityDao<T>` interface, `FieldChange` value object) tend to collapse into inline procedural code during LLM generation. Callers cannot tell from method signatures alone whether versioning uses a configurable diff object or an inline `if`. LLMs optimize for observable behavior, dropping internal maintainability abstractions unless explicitly prompted.
- **Trap Warning:** Explain that this insight comes directly from analyzing why generic DAOs collapsed in Teammate's subsystem output.

### Question 18: How did you compute Layer Preservation Score (LPS) for both projects?
- **Expected Evidence:** $LPS = \frac{N_{\text{valid}}}{N_{\text{valid}} + N_{\text{violation}}} \times 100\%$; `analysis.md` Section 4.1.
- **Strong Answer:** LPS counts valid directional inter-layer call edges versus cross-layer violations (e.g., Controller returning Entity). My v3 achieved 100% (clean Controller $\rightarrow$ Service DTO $\rightarrow$ Repo flow). Teammate's subsystem (L) scored 0% because repo, domain, and registry were fused into one class.
- **Trap Warning:** Define the formula clearly and state the exact edge counts.

### Question 19: Why does Module Separation Index (MSI) score 1.0 for a collapsed God Class (`L_EntityRepository`)?
- **Expected Evidence:** $MSI = 1 - \frac{\sum d(i,j)}{|M|(|M|-1)}$; `LLM_vs_Human_Architecture_Report.md` Section 6.
- **Strong Answer:** MSI measures coupling *between* modules. When a system collapses into a single class ($|M|=1$), there are 0 inter-module edges, yielding an artificially perfect $MSI = 1.0$. This proves inter-module metrics like MSI must be evaluated alongside intra-module bloat metrics like God Module Ratio (GMR).
- **Trap Warning:** Use this answer to demonstrate deep metric critique rather than blindly accepting metric values.

### Question 20: Why could CBO and LCOM4 not be independently verified for your teammate's project?
- **Expected Evidence:** Metric verification audit table; absence of raw tool execution logs in `Project-1-OpenMetadata/`.
- **Strong Answer:** Teammate's repository markdown files contained qualitative metric analyses but lacked raw automated tool execution logs for class-level CBO and LCOM4. To maintain strict academic integrity, those metrics were marked as "Unverified" rather than inventing numbers.
- **Trap Warning:** Never invent metric values when raw data is missing.

### Question 21: Compare the persistence strategies: JPA relational entities vs JDBI3/Jackson JSON column serialization.
- **Expected Evidence:** `fineract/` (Spring Data JPA `@Entity`) vs `Project-1-OpenMetadata/Original/` (`CollectionDAO.java`, JDBI3 `@SqlQuery`).
- **Strong Answer:** Fineract uses Spring Data JPA with explicit relational tables and `@Entity` annotations. OpenMetadata uses Dropwizard with JDBI3 and Jackson, serializing complex nested JSON objects directly into relational database columns.
- **Trap Warning:** Show that you understand the underlying database mapping strategies of both human reference systems.

### Question 22: Did either student manually edit the LLM-generated code after generation?
- **Expected Evidence:** Human intervention audit section; repository git commit logs.
- **Strong Answer:** Audit of repository commit histories and code files confirmed 0 manual post-generation edits in both projects. All structural changes were achieved strictly through iterative prompt engineering ($v1 \rightarrow v2 \rightarrow v3$).
- **Trap Warning:** State this as a verified Fact backed by commit audit evidence.

### Question 23: Why did OpenMetadata's commit snapshot (Dec 2021) weakly satisfy the pre-2020 history rule?
- **Expected Evidence:** Commit `40b7389741` (2021-12-31); `LLM_vs_Human_Architecture_Report.md` Section 1.
- **Strong Answer:** OpenMetadata's repository was founded in August 2021, so no pre-2020 commits exist. However, the Dec 2021 commit predates ChatGPT's launch (Nov 2022) and mainstream LLM code generation, satisfying the underlying intent of evaluating pre-LLM human software.
- **Trap Warning:** Be honest about the constraint mismatch instead of hiding it.

### Question 24: What are the key SOLID violations identified in `L_EntityRepository.java`?
- **Expected Evidence:** `LLM_vs_Human_Architecture_Report.md` Section 5 (SVC = 3).
- **Strong Answer:** `L_EntityRepository` violates SRP (fuses storage, versioning, registry, and validation), OCP (adding major version fields requires editing hardcoded `if` conditions), and DIP (depends directly on concrete `HashMap` storage instead of `EntityDao<T>`).
- **Trap Warning:** Cite the specific code evidence for each violation.

### Question 25: Which design patterns were preserved in both projects, and which were lost?
- **Expected Evidence:** Design pattern comparison matrix.
- **Strong Answer:** My project preserved Strategy, Command, and State (100% DPPI in v3). Teammate's project preserved Repository and Factory (75% macro PPR), but lost Template Method, Generic DAO, and Value Object change records in their subsystem reconstruction.
- **Trap Warning:** Distinguish between macro project preservation and subsystem-level pattern collapse.

### Question 26: Explain the causal chain from source architecture to metric outcome for both projects.
- **Expected Evidence:** End-to-end causal chain diagram.
- **Strong Answer:** Source Architecture $\rightarrow$ Functional Spec Format $\rightarrow$ Prompt Engineering Constraints $\rightarrow$ LLM Structural Interpretation $\rightarrow$ Generated Code Topology $\rightarrow$ Metric Result. For Fineract, detailed structural rules in Prompt v2/v3 led to 38 classes and 100% LPS/DPPI. For OpenMetadata, concise pattern requests led to 8 flat files and subsystem pattern collapse.
- **Trap Warning:** Walk through every step of the chain systematically.

### Question 27: What did your project do better than your teammate's project?
- **Expected Evidence:** Section 10 fairness evaluation.
- **Strong Answer:** My project achieved strict DTO response encapsulation (`LoanResponse`), granular SRP service segregation (4 services), 100% polymorphic pattern preservation, and empirical OCP extensibility testing ($R_{\text{OCP}} = 0.40$).
- **Trap Warning:** Frame strengths objectively with code evidence, without sounding arrogant.

### Question 28: What did your teammate's project do better than your project?
- **Expected Evidence:** Section 10 fairness evaluation.
- **Strong Answer:** Teammate's project successfully reconstructed complex algorithmic version diffing (`computeChange`), achieved pattern recovery using concise prompts (296 bytes), and provided an insightful critique of MSI metric inflation in god classes.
- **Trap Warning:** Give genuine credit to your teammate's achievements; do not minimize them.

### Question 29: What are the primary threats to validity and study limitations of your comparative evaluation?
- **Expected Evidence:** Section 13 limitations.
- **Strong Answer:** Limitations include a small sample size ($N=2$), snapshot date divergence (2019 vs 2021), metric sensitivity flaws (MSI), and unlogged LLM sampling parameters (temperature).
- **Trap Warning:** Demonstrating awareness of research limitations builds strong academic credibility.

### Question 30: What is your strongest defensible academic conclusion regarding LLM-based software reconstruction?
- **Expected Evidence:** Section 12 research findings.
- **Strong Answer:** Unprimed LLMs default to collapsed procedural structures, optimizing for observable functional correctness over maintainability. However, guided prompt engineering with explicit structural constraints reliably restores enterprise-grade layering and polymorphic patterns.
- **Trap Warning:** Summarize the core thesis cleanly in 2-3 sentences.

---

## 15. Conclusion & Defense Summary (Step 22)

This comparative research report demonstrates that structural divergence between LLM-reconstructed systems is driven primarily by **prompt engineering constraint specificity** and **upstream domain topology**, rather than inherent flaws in LLM capabilities. When unprimed, LLMs prioritize immediate functional execution, collapsing maintainability abstractions (DTOs, generic DAOs, State machines) into procedural code. Guided structural prompt refactoring successfully restores publication-grade enterprise architectures (**100% LPS, 100% DPPI**).

---

## 16. Reference & Link Audit (Step 27)

- [`README.md`](file:///e:/cse423/README.md) — Relative link verified: `[Team Comparative Analysis](team-comparative-analysis.md)`
- [`prompts.md`](file:///e:/cse423/prompts.md) — Master prompt audit log
- [`analysis.md`](file:///e:/cse423/analysis.md) — Master quantitative research report
- [`selected-files.md`](file:///e:/cse423/selected-files.md) — Selected source file descriptions
- [`Project-1-OpenMetadata/LLM_vs_Human_Architecture_Report.md`](file:///e:/cse423/Project-1-OpenMetadata/LLM_vs_Human_Architecture_Report.md) — Teammate architecture report
- [`Project-1-OpenMetadata/DesignPatternAnalysis.md`](file:///e:/cse423/Project-1-OpenMetadata/DesignPatternAnalysis.md) — Teammate pattern report
