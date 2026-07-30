# CSE 423 — LLM Prompts Used (Master File)

> **Important:** This file is the single source of truth. Every prompt listed
> here is the *exact* prompt that was run against the LLM to generate the
> corresponding project code. Do not edit the project without also updating
> the matching prompt version here, and vice versa.

Reference repository: **Apache Fineract** (https://github.com/apache/fineract)
Pre-LLM snapshot commit: `ba6f778d8` (last commit before 2020-01-01)

---

## TASK 1: Architecture Structure Preservation

### Prompt v1 — Initial Functional Description

**Status:** ACTIVE — used to generate `llm-generated/task1-architecture/v1/`
**Date run:** 2026-07-26
**Prompt Technique:** Zero-Shot Functional Requirement Prompt (Unprimed Baseline)
**Reasoning:** Deliberately avoids architectural vocabulary ("layer",
"controller", "service", "repository") so the LLM's default structural
choices can be observed without priming.

```
Build a loan account management feature for a banking system, exposed as a
REST API. The system should support the following:

1. A client or group can apply for a new loan — providing loan product,
   amount, interest rate, term, repayment frequency, and similar details.

2. An admin or loan officer can approve, reject, or withdraw an application.

3. An approved loan can be disbursed, and disbursement can be undone if
   needed.

4. Each loan automatically gets a repayment schedule calculated —
   installment amount, due dates, and interest breakdown.

5. When a client makes a repayment/transaction, it gets recorded and the
   schedule updates accordingly.

6. Charges (fees) can be added to a loan, and a loan summary (total due,
   total paid, total outstanding) should reflect those charges.

7. Guarantors can be added to a loan, and recovery from a guarantor should
   be possible if needed.

8. Each loan account has a status lifecycle: submitted → approved → active
   → closed/overdue/written-off — and each state transition must follow
   valid rules.

9. A user can view details of a specific loan, or a filterable/paginated
   list of all loans.

10. The assigned loan officer can be reassigned.

Implement this system in Java using Spring Boot, and provide a complete,
runnable codebase. Give each class/file in a separate code block.
```

**Output observed:**
- LLM produced a complete, multi-file Spring Boot project (~20 files):
  `domain/` (entities + enums), `repository/` (JPA interfaces),
  `service/` (single `LoanScheduleGenerator` + single `LoanService`),
  `dto/` (request objects only), `controller/` (`LoanController`,
  `LoanOfficerController`), plus main class and a test class.
- **Layer Preservation:** Controller → Service → Repository → Entity chain
  is present and correctly directional (matches Fineract's api → service →
  domain flow). LPS is high at this level.
- **Gap 1 — CQRS split collapsed:** Fineract splits Read/Write services
  per domain concern (20+ classes); the LLM output merged all read and
  write logic into a single `LoanService` class ("God Service").
- **Gap 2 — DTO/entity boundary violated:** Fineract's controller returns
  `LoanAccountData` (a read-only DTO); the LLM's `LoanController` returns
  the raw `Loan` JPA `@Entity` directly in `ResponseEntity<Loan>` — a
  Cross-Layer Violation (persistence layer leaking into the API contract).
- **Command pattern absent:** Fineract wraps every mutation in a
  `CommandWrapper` dispatched through a single audited entry point; the
  LLM output calls plain service methods directly with no such
  abstraction (relevant to Task 2 as well).

---

### Prompt v2 — Refinement

**Status:** ACTIVE — used to generate `llm-generated/task1-architecture/v2/`
**Date run:** 2026-07-26
**Prompt Technique:** Constraint Priming & Boundary Refinement (CQRS + DTO Rules)
**Gap this targets:** (1) single God Service instead of Read/Write split,
(2) JPA entity exposed directly through the API instead of a response DTO.
**Change made vs v1:** Added two explicit structural constraints (CQRS
split, response-object boundary) while explicitly holding all functional
requirements constant, to isolate the effect of the constraint from any
unrelated drift in output.

```
Thanks — now refine the loan management system you just built with the
following two structural changes:

1. SEPARATE READ AND WRITE RESPONSIBILITIES INTO DIFFERENT SERVICE CLASSES.
   Right now, one LoanService class handles both fetching/querying data
   (e.g. getLoanDetails, getFilteredLoans) and mutating data (e.g.
   approveLoan, disburseLoan, makeRepayment). Split these into two separate
   service classes:
   - A read-only service responsible only for retrieving and querying loan
     data, with no side effects.
   - A write/command service responsible only for state changes and
     mutations, with no query methods.
   Update the controller to depend on both services instead of one, calling
   the appropriate one for each operation.

2. DO NOT EXPOSE PERSISTENCE ENTITIES DIRECTLY THROUGH THE API.
   Right now, controller methods return the JPA @Entity classes (e.g. Loan)
   directly as the HTTP response body. This couples your database schema to
   your API contract. Instead:
   - Create dedicated response/view objects that represent exactly what the
     API should expose to clients.
   - Map entities to these response objects inside the read service (not in
     the controller).
   - Controllers should return only these response objects, never the raw
     JPA entities.

Keep every functional requirement from before unchanged — this is purely a
structural refactor, not a change in what the system does. Provide the
complete updated codebase, with each class/file in its own code block, and
clearly mark which files are new and which are modified.
```

**Output observed:**
- Both targeted gaps were resolved:
  - `LoanReadService` (read-only, `@Transactional(readOnly = true)`) and
    `LoanWriteService` (mutations) are now fully separate classes — the
    CQRS-style split now mirrors Fineract's Read/Write service convention.
  - Controllers now return `LoanResponse`/`LoanOfficerResponse` DTOs;
    entity-to-DTO mapping happens inside `LoanReadService.mapToResponse()`
    as instructed. No JPA entity is exposed through the API anymore.
- **Remaining gap:** `LoanWriteService` is still a single class handling
  9+ unrelated responsibility groups (lifecycle transitions, financial
  transactions, officer administration) — a smaller-scale "God Service"
  persists even after the CQRS split. Fineract distributes these across
  20+ domain-specific service classes.

---

### Prompt v3 — Final Refinement

**Status:** ACTIVE — used to generate `llm-generated/task1-architecture/v3/`
**Date run:** 2026-07-26
**Prompt Technique:** Single Responsibility Decomposition Priming
**Gap this targets:** `LoanWriteService` still bundles multiple unrelated
responsibility groups into one class instead of being decomposed by
concern, unlike Fineract's fine-grained per-domain service split.
**Change made vs v2:** Requested decomposition of the write service into
smaller, single-responsibility service classes grouped by concern
(lifecycle, financial transactions, administration), while explicitly
holding functional behavior constant.

```
Good — the read/write separation and DTO boundary look correct now. One
more refinement:

LoanWriteService currently handles many unrelated responsibility groups in
a single class: loan application/lifecycle transitions (submit, approve,
reject, withdraw, disburse, undo-disburse, write-off), financial
operations (charges, repayments, guarantor recovery), and administrative
operations (officer reassignment).

Split LoanWriteService into smaller, single-responsibility service classes
based on these concern groups — for example, one service for lifecycle
transitions, one for financial transactions, and one for administrative
operations like officer reassignment. Each should depend only on the
repositories it actually needs. Update the controller to use whichever of
these services is appropriate for each endpoint.

Keep all functional behavior identical — this is a structural refactor
only. Provide the complete updated codebase.
```

**Output observed:**
- `LoanWriteService` was decomposed into three single-responsibility
  services: `LoanLifecycleService` (submit/approve/reject/withdraw/
  disburse/undo/write-off), `LoanTransactionService` (charges, repayments,
  guarantor recovery), and `LoanAdminService` (officer reassignment).
- A new `LoanTransactionProcessor` component was also factored out to hold
  shared calculation logic (charge distribution, transaction reapplication,
  summary recalculation) used by both Lifecycle and Transaction services —
  this is a spontaneous Single-Responsibility refinement beyond what was
  explicitly requested.
- Controller now depends on four narrow services instead of one broad
  service, mirroring (at a smaller scale) Fineract's pattern of many
  focused, domain-specific service classes rather than one large service.
- No functional regressions — same lifecycle rules, same transaction
  ordering (charges → interest → principal) preserved across all 3
  iterations.

---

## TASK 2: Design Pattern Preservation

### Prompt v1 — Initial Functional Description

**Status:** ACTIVE — used to generate `llm-generated/task2-design-patterns/v1/`
**Date run:** 2026-07-26
**Prompt Technique:** Behavioral Priming (Implicit Pattern Need)
**Reasoning:** Avoids naming any pattern directly (Strategy, Factory,
Command, State). Each requirement implies the *need* a pattern solves, so
pattern adoption (or its absence) in the output can be measured honestly.

```
Design a loan repayment system where:

1. A loan's repayment should be allocated differently depending on the
   loan product — for example, some products deduct interest before
   principal, others deduct principal before interest, and others deduct
   penalty charges first. This allocation logic must be selectable at
   runtime based on the loan product's configuration, and it should be easy
   to add new allocation rules in the future without modifying existing
   code.

2. Users can perform various actions on a loan — submit, approve, reject,
   withdraw, disburse, undo disbursement, reassign loan officer. Each
   action should have its own validation, execution logic, and audit trail.
   All action types should be processed through a single generic mechanism,
   so that adding a new action type is easy and each action can be tracked
   /logged individually.

3. A loan has a status (submitted, approved, active, closed, overdue,
   written-off), and which actions are allowed in each status must be
   strictly enforced — invalid state transitions must not be allowed.

Implement this system in Java using Spring Boot, and provide a complete,
runnable codebase. Give each class/file in a separate code block.
```

**Internal mapping (for analysis only — NOT shown to the LLM):**
- Requirement 1 → Strategy / Factory pattern
- Requirement 2 → Command pattern
- Requirement 3 → State pattern

**Output observed:**
- **Strategy Pattern — PRESERVED:** `RepaymentAllocationStrategy` interface
  with 3 concrete implementations (`ChargesInterestPrincipalStrategy`,
  `PrincipalInterestChargesStrategy`, `InterestPrincipalChargesStrategy`),
  selected at runtime via `RepaymentStrategyRegistry` keyed by loan
  product — a genuine Strategy pattern, matching Fineract's
  `LoanRepaymentScheduleTransactionProcessor` family.
- **Command Pattern — PRESERVED:** `LoanActionHandler<T>` interface acts as
  the Command abstraction, with 12 concrete handler classes (one per
  action type) as ConcreteCommands, and `LoanActionDispatcher` as the
  Invoker. The LLM spontaneously added a `LoanActionAudit` entity that
  records every dispatch attempt (success/failure) — this closely mirrors
  Fineract's `CommandWrapper` + audited `logCommandSource()` mechanism,
  even though "audit trail" in the prompt was the only hint given.
- **State Pattern — COLLAPSED:** Requirement 3 was implemented as a plain
  `switch` statement (`validateStateTransition()`) inside
  `LoanActionDispatcher`, using procedural conditional branches per
  `LoanStatus` value. This is functionally correct (invalid transitions
  are blocked) but is **not the State design pattern** — Fineract's
  `LoanLifecycleStateMachine` is a polymorphic abstraction; the LLM
  defaulted to conditional logic instead of introducing a `State`
  interface with per-status implementations.

**Preliminary pattern count:** 2 of 3 targeted patterns preserved as true
OOP patterns; 1 collapsed into conditional/procedural code.

---

### Prompt v2 — Refinement

**Status:** ACTIVE — used to generate `llm-generated/task2-design-patterns/v2/`
**Date run:** 2026-07-26
**Prompt Technique:** Architectural Pattern Enforcement Priming (State Pattern)
**Gap this targets:** State transition rules implemented as a procedural
`switch` statement instead of the State design pattern (polymorphic
per-status behavior).
**Change made vs v1:** Added an explicit structural constraint requesting
polymorphic state representation, again without naming "State pattern"
directly — instead describing the property the pattern provides (each
status owning its own allowed-actions logic, extensible without touching
a central switch).

```
Good — the allocation strategy and action dispatch mechanism both look
solid. One more refinement, focused on requirement 3 (status-based action
rules):

Currently, which actions are valid for a given loan status is decided by
a single large conditional block that checks the status and switches on
it. Refactor this so that each loan status is represented by its own
object that knows which actions are valid for it, rather than being
checked in one central conditional. Adding a new status in the future
should mean adding one new status-representing class, not editing a
shared switch/if-else block. The dispatcher should simply ask the current
status object whether an action is allowed, without containing per-status
logic itself.

Keep all functional behavior and audit logging identical — this is a
structural refactor only. Provide the complete updated codebase.
```

**Output observed:**
- The State pattern was correctly introduced: `LoanStatusState` interface
  with 8 concrete implementations, one per `LoanStatus` value
  (`SubmittedStatusState`, `ApprovedStatusState`, `ActiveStatusState`,
  `OverdueStatusState`, `ClosedStatusState`, `RejectedStatusState`,
  `WithdrawnStatusState`, `WrittenOffStatusState`).
- `LoanActionDispatcher` no longer contains the conditional `switch` block
  — it now autowires all `LoanStatusState` beans and delegates the
  "is this action allowed?" question to the matching state object, closely
  matching the polymorphic delegation Fineract's
  `LoanLifecycleStateMachine` performs.
- **All three targeted patterns are now preserved as genuine OOP
  patterns**: Strategy (v1), Command (v1), State (v2). This closes the gap
  identified after Prompt v1.

---

### Prompt v3 — Final Refinement (Extensibility / OCP verification)

**Status:** ACTIVE — used to generate `llm-generated/task2-design-patterns/v3/`
**Date run:** 2026-07-26
**Prompt Technique:** Open-Closed Principle Stress-Testing Prompt
**Gap this targets:** Not a defect fix — v1 and v2 already introduced all
three target patterns. v3 instead tests whether the patterns were
implemented *correctly* by verifying Open/Closed compliance: does adding
a new status/action require touching existing classes, or only adding
new ones? This is the actual payoff a State/Command/Strategy refactor is
supposed to deliver, so it is the right final check before metrics.
**Change made vs v2:** Instead of another structural constraint, this
prompt asks the LLM to *extend* the system with a new case, which
reveals whether the earlier refactors were genuinely extensible or only
superficially pattern-shaped.

```
Let's verify the design holds up under a real extension. Add a new loan
action, FREEZE, and a new loan status, FROZEN, with these rules:

- FREEZE is only allowed from ACTIVE or OVERDUE status, and moves the
  loan to FROZEN.
- While FROZEN, no repayments, charges, guarantor actions, or officer
  reassignment are allowed except UNFREEZE, which returns the loan to
  ACTIVE.

Implement this addition. As you do it, do not modify the logic inside any
existing status class or existing action handler — only add new files
(a new status class, a new action type, and its handler) and the minimal
wiring needed for the system to recognize them (e.g. registering the new
enum value). If implementing this requires changing the internals of an
existing status or handler class, explain why the current design could
not accommodate the extension cleanly.
```

**Output observed:**
- New `FROZEN` status was added by creating a single new class
  (`FrozenStatusState`) with **zero modification** to any other existing
  status class — the State refactor delivers genuine Open/Closed
  compliance on the "new status" axis.
- New `FREEZE`/`UNFREEZE` actions, however, **required editing two
  existing classes** (`ActiveStatusState`, `OverdueStatusState`) to allow
  the new action from those statuses — the design is *not* OCP-compliant
  on the "new action" axis.
- The LLM self-diagnosed this as an instance of the **Expression
  Problem**: because valid-action rules are stored inside each status
  class (organized by status), adding a new status is free but adding a
  new action requires touching every status class that should permit it.
  It also proposed the correct fix direction (externalize the
  status×action mapping instead of hard-coding it per status class).
- This is a genuine, verifiable structural insight — not something staged
  by the prompt, since the prompt only asked for the extension and an
  explanation *if* modification was needed.

---

## Change Log

| Date | Task | Version | What changed | Why |
|------|------|---------|--------------|-----|
| 2026-07-26 | Task 1 | v1 | Initial prompt created | Baseline functional prompt, no architecture hints |
| 2026-07-26 | Task 1 | v2 | Added CQRS and DTO rules | Fixed God Service and Entity exposure violations |
| 2026-07-26 | Task 1 | v3 | Decomposed Write service | Segregated concerns into Lifecycle, Transaction, Admin |
| 2026-07-26 | Task 2 | v1 | Initial pattern prompt | Tested unprimed adoption of Strategy, Command, State |
| 2026-07-26 | Task 2 | v2 | Requested state objects | Converted procedural switch into State pattern |
| 2026-07-26 | Task 2 | v3 | Added FREEZE action & status | Tested Open-Closed compliance & Expression Problem |

