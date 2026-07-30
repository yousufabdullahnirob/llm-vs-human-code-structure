# Selected Source Files & Comprehensive Architectural Descriptions

**Course:** CSE 423 — Software Engineering Structure Analysis  
**Repository:** `yousufabdullahnirob/llm-vs-human-code-structure`  

This document provides complete descriptions of all selected source files from the chosen reference project (**Apache Fineract**) and the reconstructed system (**LLM Generated System**), fulfilling the assignment requirement for file-by-file structural documentation.

---

## 1. Human Reference Project: Apache Fineract

**Official GitHub URL:** [https://github.com/apache/fineract](https://github.com/apache/fineract)  
**Snapshot Commit Hash:** `ba6f778d8c39e248b6c43bf0848039ed678fa45d` (Date: 2019-12-18)  
**Local Directory:** [fineract/](file:///e:/cse423/fineract/)  

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                  APACHE FINERACT SELECTED FILES DIRECTORY                                               |
+----+--------------------------------------------+-----------------------+---------------------+-------------------------+
| #  | File Name                                  | Package               | Layer               | Primary Role            |
+----+--------------------------------------------+-----------------------+---------------------+-------------------------+
| 1  | LoansApiResource.java                      | .portfolio.loanaccount| Controller (API)    | REST API Endpoint       |
| 2  | LoanReadPlatformServiceImpl.java           | .portfolio.loanaccount| Service (Read Side) | CQRS Read Query Engine  |
| 3  | LoanWritePlatformServiceJpaRepositoryImpl  | .portfolio.loanaccount| Service (Write Side)| CQRS State Mutation     |
| 4  | LoanAccountData.java                       | .portfolio.loanaccount| DTO (Data Boundary) | Read-only API DTO       |
| 5  | Loan.java                                  | .portfolio.loanaccount| Domain Entity       | JPA Core Entity         |
| 6  | CommandWrapper.java                        | .commands.domain      | Command Invoker     | Generic Command Wrapper |
| 7  | LoanLifecycleStateMachineImpl.java         | .portfolio.loanaccount| State Machine       | Lifecycle Validation    |
| 8  | LoanRepaymentScheduleTransactionProcessor  | .portfolio.loanaccount| Strategy Allocation | Repayment Breakdown     |
+----+--------------------------------------------+-----------------------+---------------------+-------------------------+
```

### Detailed File Descriptions

#### 1. `LoansApiResource.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.api`
- **Architectural Layer:** Controller / API Presentation Layer
- **Purpose:** Serves as the primary HTTP REST controller exposing core banking loan management operations.
- **Key Responsibilities:** Validates incoming JSON payloads, authenticates requests, delegates read requests to `LoanReadPlatformService` and write mutations to `PortfolioCommandSourceWritePlatformService`.
- **Important Methods:** `retrieveAll()`, `retrieveOne()`, `stateTransitions()`, `calculateLoanScheduleOrSubmitLoanApplication()`.
- **Assignment Role:** Serves as the human reference benchmark for REST API controller design, evaluated against LLM-generated `LoanController.java`.

#### 2. `LoanReadPlatformServiceImpl.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.service`
- **Architectural Layer:** CQRS Read Service Layer
- **Purpose:** Implements side-effect-free data queries for loan accounts.
- **Key Responsibilities:** Executes raw SQL queries using Spring `JdbcTemplate` for high-performance reading, maps database rows directly into read-only `LoanAccountData` DTOs, enforcing strict read/write separation.
- **Important Methods:** `retrieveAll()`, `retrieveOne()`, `retrieveRepaymentSchedule()`.
- **Assignment Role:** Benchmark for CQRS query side segregation, evaluated against LLM `LoanReadService.java`.

#### 3. `LoanWritePlatformServiceJpaRepositoryImpl.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.service`
- **Architectural Layer:** CQRS Write Service Layer
- **Purpose:** Handles all state mutations, financial transactions, and loan lifecycle transitions.
- **Key Responsibilities:** Manages transaction boundaries (`@Transactional`), persists entity mutations via Spring Data JPA, enforces domain validation rules during disbursements, repayments, and write-offs.
- **Important Methods:** `submitApplication()`, `approveApplication()`, `disburseLoan()`, `makeRepayment()`.
- **Assignment Role:** Benchmark for CQRS command side mutation, evaluated against LLM `LoanLifecycleService` and `LoanTransactionService`.

#### 4. `LoanAccountData.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.data`
- **Architectural Layer:** Data Transfer Object (DTO) Boundary Layer
- **Purpose:** Immutable data carrier holding read-only representations of loan details.
- **Key Responsibilities:** Encapsulates loan state without exposing internal JPA entity annotations or persistence behavior to external API clients.
- **Assignment Role:** Benchmark for Data Encapsulation, evaluated against LLM Prompt v1 entity leaks vs. Prompt v2 `LoanResponse.java`.

#### 5. `Loan.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.domain`
- **Architectural Layer:** Domain Persistence Entity Layer
- **Purpose:** Core JPA entity modeling the loan account lifecycle and transaction history.
- **Key Responsibilities:** Encapsulates financial calculations (interest, principal, charge balances), maintains list of `RepaymentScheduleInstallment` entities, enforces internal domain invariants.
- **Assignment Role:** Evaluated to observe whether LLMs expose entities directly or encapsulate them behind DTO boundaries.

#### 6. `CommandWrapper.java`
- **Package:** `org.apache.fineract.commands.domain`
- **Architectural Layer:** Command Invoker / Audit Layer
- **Purpose:** Encapsulates every mutating API action into a uniform, serializable command object.
- **Key Responsibilities:** Captures user identity, entity ID, action name, and JSON payload for central audit logging before dispatching to write services.
- **Assignment Role:** Benchmark for GoF Command Pattern preservation, evaluated against LLM `LoanActionDispatcher.java`.

#### 7. `LoanLifecycleStateMachineImpl.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.service`
- **Architectural Layer:** State Machine / Domain Layer
- **Purpose:** Enforces valid loan status transitions (Submitted $\rightarrow$ Approved $\rightarrow$ Active $\rightarrow$ Closed/Written-Off).
- **Key Responsibilities:** Evaluates transition matrices, blocks invalid status changes.
- **Assignment Role:** Benchmark for State Machine design, evaluated against LLM `LoanStatusState.java` polymorphic state objects.

#### 8. `LoanRepaymentScheduleTransactionProcessor.java`
- **Package:** `org.apache.fineract.portfolio.loanaccount.domain`
- **Architectural Layer:** Strategy Allocation Layer
- **Purpose:** Abstract strategy interface defining how incoming repayments are allocated across charges, interest, and principal.
- **Assignment Role:** Benchmark for GoF Strategy Pattern preservation, evaluated against LLM `RepaymentAllocationStrategy.java`.

---

## 2. LLM-Generated Project: Reconstructed Banking System

**Official Repository Location:** [llm-generated/](file:///e:/cse423/llm-generated/)  
**Technology Stack:** Java 17, Spring Boot 3.2.0, Spring Data JPA, H2 Database  

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                    LLM GENERATED SELECTED FILES DIRECTORY                                               |
+----+-----------------------------------+-----------------------------+-----------------------+--------------------------+
| #  | File Name                         | Package                     | Layer                 | Demonstrated Concept     |
+----+-----------------------------------+-----------------------------+-----------------------+--------------------------+
| 1  | LoanController.java               | .controller                 | Controller (API)      | DTO Encapsulation        |
| 2  | LoanReadService.java              | .service                    | CQRS Read Service     | @Transactional(readOnly) |
| 3  | LoanLifecycleService.java         | .service                    | CQRS Write Service    | Domain State Mutation    |
| 4  | LoanResponse.java                 | .dto.response               | DTO Boundary          | API Response Mapping     |
| 5  | LoanActionDispatcher.java         | .service.command            | Command Invoker       | Command Pattern + Audit  |
| 6  | LoanActionHandler.java            | .service.command            | Command Interface     | Generic Action Command   |
| 7  | LoanStatusState.java              | .service.status             | State Interface       | Polymorphic State Pattern|
| 8  | ActiveStatusState.java            | .service.status             | Concrete State        | OCP Modification Point   |
| 9  | FrozenStatusState.java            | .service.status             | Concrete State        | OCP Extension Point      |
| 10 | RepaymentAllocationStrategy.java  | .service.strategy           | Strategy Interface    | Strategy Pattern         |
| 11 | RepaymentStrategyRegistry.java    | .service.strategy           | Strategy Factory      | Dynamic Strategy Lookup  |
+----+-----------------------------------+-----------------------------+-----------------------+--------------------------+
```

### Detailed File Descriptions

#### 1. [LoanController.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/controller/LoanController.java)
- **Package:** `com.bank.loan.controller`
- **Architectural Layer:** Controller / API Layer
- **Purpose:** REST API entry point exposing endpoints for loan applications, approvals, disbursements, repayments, and officer reassignments.
- **Why Selected:** Demonstrates restoration of the DTO encapsulation boundary in Prompt v2. Returns `ResponseEntity<LoanResponse>` instead of leaking database entities.
- **Dependencies:** `LoanReadService`, `LoanActionDispatcher`, `LoanResponse`.

#### 2. [LoanReadService.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/service/LoanReadService.java)
- **Package:** `com.bank.loan.service`
- **Architectural Layer:** Service Layer (Query Side)
- **Purpose:** Read-only service isolated from state mutations.
- **Why Selected:** Demonstrates CQRS separation induced by Prompt v2. Marked with `@Transactional(readOnly = true)` and maps entities to response DTOs.

#### 3. [LoanActionDispatcher.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/service/command/LoanActionDispatcher.java)
- **Package:** `com.bank.loan.service.command`
- **Architectural Layer:** Command Invoker / Auditing Layer
- **Purpose:** Invoker component in the Command pattern. Autowires all `LoanActionHandler<?>` beans, validates status transitions via `LoanStatusState`, and logs every execution attempt to `LoanActionAuditRepository`.
- **Why Selected:** Demonstrates spontaneous adoption of GoF Command pattern and audit logging.

#### 4. [LoanStatusState.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/service/status/LoanStatusState.java)
- **Package:** `com.bank.loan.service.status`
- **Architectural Layer:** State Pattern Interface
- **Purpose:** State interface defining polymorphic status validation (`isActionAllowed()`, `transition()`).
- **Why Selected:** Demonstrates recovery of the State design pattern in Prompt v2, replacing procedural `switch` blocks with polymorphic state objects.

#### 5. [FrozenStatusState.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/service/status/FrozenStatusState.java)
- **Package:** `com.bank.loan.service.status`
- **Architectural Layer:** Concrete State Implementation
- **Purpose:** Concrete state representing a frozen loan account.
- **Why Selected:** Used in Prompt v3 OCP stress test. Added with zero edits to existing state classes ($\text{OCP Compliant}$ for new states).

#### 6. [RepaymentAllocationStrategy.java](file:///e:/cse423/llm-generated/task2-design-patterns/v3/src/main/java/com/bank/loan/service/strategy/RepaymentAllocationStrategy.java)
- **Package:** `com.bank.loan.service.strategy`
- **Architectural Layer:** Strategy Pattern Interface
- **Purpose:** Strategy interface for allocating incoming repayments across principal, interest, and fee charges.
- **Why Selected:** Demonstrates spontaneous preservation of GoF Strategy pattern across all prompt iterations.
