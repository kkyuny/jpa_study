## Ch07. 영속성이란?

### 1. IL
#### **영속성 컨텍스트와 관련된 개념**
- **영속성 컨텍스트**: JPA에서 엔티티를 감시/관리하여 데이터를 영속화하는 데 사용하는 컨테이너.
- **EntityManager**: 영속성 컨텍스트를 직접 관리하는 주체.  
- 스프링부트에서는 **spring-boot-starter-data-jpa**가 기본 설정을 제공.

#### **JPA 설정**
- **Hibernate의 옵션**:
  - `ddl-auto`: Hibernate에서 제공하는 세부 옵션으로 테이블 생성, 변경 등을 제어.
  - `generate-ddl`: JPA 구현체와 상관없이 사용할 수 있는 범용적인 옵션.
- **DataSource 초기화**:
  - `spring.datasource.initialization-mode`: 데이터 초기화 방식 설정.
- **충돌 시 우선순위**: 여러 옵션이 충돌하면 설정의 우선순위를 고려해야 함.

---

### 2. JPA 캐시와 플러시
- **Entity 캐시**:
  - JPA의 1차 캐시는 특정 엔티티의 ID 기반으로 데이터를 캐싱하여 성능을 최적화.
  - 데이터베이스 쿼리를 줄이고, ID 기반 조회, 업데이트, 삭제에서 성능 향상을 제공.
- **Flush**:
  - **명시적 플러시**: `flush()` 메서드로 DB에 영속화.
  - **자동 플러시**: 트랜잭션 종료 시 자동으로 발생.
- **주의점**: 영속성 컨텍스트와 DB 데이터 간의 상태 차이(Dirty Checking) 발생 가능.
  - Dirty Checking: 데이터 조회 시 캐시에 저장 된 값과 실제 DB에 저장 된 값에 차이가 있을 수 있음.  

---

### 3. Entity의 생애주기
엔티티의 상태 변화는 4단계로 나뉨:
1. **비영속(Transient)**: 영속성 컨텍스트와 연관되지 않은 상태.
2. **영속(Persistent)**: 영속성 컨텍스트에 관리되는 상태.
3. **준영속(Detached)**: 영속성 컨텍스트에서 분리된 상태.
   - `merge()`를 통해 다시 영속 상태로 복구 가능.
4. **삭제(Removed)**: 삭제된 상태로, DB에서 제거될 예정.

---

### 4. 트랜잭션(ACID)
- **트랜잭션**: 데이터 상태 변경을 위한 논리적 단위.
- **ACID 속성**:
  - **원자성(Atomicity)**: All or Nothing. 부분적 성공 허용 불가.
    - ex) 내가 송금을 하면 상대방은 송금을 받아야한다.
  - **일관성(Consistency)**: 데이터 정합성 유지.
    - ex) 내가 송금을 한 금액과 상대방이 송금을 받은 금액은 같아야한다.
  - **독립성(Isolation)**: 트랜잭션 간의 독립성 보장.
    - ex) 송금이 완료되기까지 상대방의 잔고에 다른 작업을 허용하지 않아야한다.
  - **지속성(Durability)**: 트랜잭션 완료 시 데이터는 영구적으로 반영.

---

### 5. @Transactional 어노테이션
- **기본 동작**
  - 트랜잭션 내 모든 쿼리가 성공하면 commit, 예외 발생 시 rollback.
- **롤백 동작**
  - 기본적으로 런타임 예외(`RuntimeException`) 발생 시 rollback.
  - 체크 예외(`Checked Exception`)는 명시적으로 처리해야 rollback 가능:
    ```java
    @Transactional(rollbackFor = Exception.class)
    ```
- **트랜잭션 전파와 예외**
  - 메서드가 다른 메서드를 호출할 경우, 호출된 메서드의 `@Transactional`은 적용되지 않음(스프링 AOP 특성).

---

### 6. 트랜잭션의 격리 수준(Isolation)과 전파(Propagation)

#### **격리 수준(Isolation)**
- **DEFAULT**: DBMS의 기본 설정 사용 (MySQL은 `REPEATABLE_READ`).
- **READ_UNCOMMITTED**: 커밋되지 않은 데이터 읽기 허용(Dirty Read 가능).
- **READ_COMMITTED**: 커밋된 데이터만 읽기 허용.
- **REPEATABLE_READ**: 트랜잭션 내 동일 데이터를 반복 조회 시 동일한 결과 제공.
- **SERIALIZABLE**: 트랜잭션 간 완전한 격리. 락을 통해 순차적 처리.

#### **전파(Propagation)**:
- **REQUIRED**: 기본 설정. 기존 트랜잭션을 재사용, 없으면 새로 생성.
- **REQUIRES_NEW**: 기존 트랜잭션과 별도로 새로운 트랜잭션 생성.
- **NESTED**: 상위 트랜잭션 내에 중첩 트랜잭션 생성.
- **SUPPORTS**: 기존 트랜잭션이 있으면 참여, 없으면 트랜잭션 없이 실행.
- **NOT_SUPPORTED**: 트랜잭션 없이 실행.
- **MANDATORY**: 기존 트랜잭션이 있어야 하며, 없으면 예외 발생.
- **NEVER**: 트랜잭션이 있으면 예외 발생.

---

### 7. 트랜잭션 범위
- **클래스 스코프**: 클래스에 선언된 트랜잭션이 모든 메서드에 적용.
- **메서드 스코프**: 클래스의 트랜잭션 설정보다 메서드의 설정이 우선 적용.

---

### 8. 코드 예시
```java
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne
@ToString.Exclude
private Users user;
