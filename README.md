# JPA vs MyBatis 비교 및 DDD 관련 정리

## ✅ 1. 핵심 차이 요약

| 항목            | JPA                                 | MyBatis                              |
|-----------------|--------------------------------------|---------------------------------------|
| 설계 패러다임   | 객체 지향 중심                       | SQL 중심                              |
| 쿼리 작성       | 자동 생성 (JPQL, Criteria 등)        | 수동 작성 (XML, Annotation)          |
| 러닝 커브       | 높음 (영속성 컨텍스트 등 이해 필요) | 낮음 (SQL 위주 사고와 친화적)        |
| 복잡 쿼리 대응 | 약함 (네이티브 필요)                | 강함 (복잡한 SQL 직접 작성)         |
| 성능 튜닝       | ORM 설정 전략 중심                  | SQL로 직접 튜닝                      |
| 생산성          | CRUD 자동화로 빠름                  | 반복 코드 많음                        |
| 디버깅          | 자동 쿼리 추적 어려움               | SQL이 명시적이라 쉬움                |


## ✅ 2. 왜 JPA를 사용하는가?

- 반복적인 CRUD 로직 제거 → 생산성 향상
- 객체지향 모델에 기반한 코드 구성 → DDD와 잘 맞음
- 연관관계 매핑, 변경 감지, 1차 캐시 등 고급 기능 제공
- DB 벤더 독립성 확보 (JPQL 기준)

하지만 복잡한 쿼리는 결국 네이티브 SQL 사용 필요.

## ✅ 3. DDD 관점에서의 차이

| 항목                        | JPA                                   | MyBatis 전통 스타일                  |
|-----------------------------|----------------------------------------|--------------------------------------|
| 도메인 모델 중심 개발       | 자연스럽게 가능                        | 수동으로 구성해야 함                 |
| 로직의 위치                 | 엔티티 내부                            | 대부분 서비스 계층                   |
| 도메인 간 연관관계 표현     | `@OneToMany`, `@ManyToOne` 등 자동화 | SQL 조인 및 수동 매핑                |
| 애그리거트 응집             | 용이함                                 | 로직 분산될 위험 있음                |

→ **MyBatis도 DDD는 가능**하지만, 적극적으로 구조를 짜야 함


## ✅ 결론  
- JPA와 MyBatis는 각각 **잘 맞는 프로젝트 환경**이 다르다.

### JPA가 잘 어울리는 환경
- **비즈니스 규칙이 복잡**하고 도메인 간 **연관관계가 많음**
- **도메인 모델이 명확하고 고유한 용어**로 정리될 수 있음
- **요구사항 변경이 잦고 진화 가능성**이 높은 경우
- **도메인 중심 구조**를 설계하거나 **마이크로서비스로 분리**할 가능성 있음
- 시스템이 **장기 운영**되며, 유지보수성과 확장성이 중요한 경우

### MyBatis가 잘 어울리는 환경
- **SQL 제어가 중요한 시스템** (복잡한 쿼리, 성능 튜닝 등)
- **DB 설계가 핵심**이거나, **데이터 중심**의 구조인 경우
- **정형화된 업무 로직**, 단순 CRUD 기반의 시스템
- 기존 시스템의 **DB 구조나 SQL을 그대로 사용해야 하는 경우**
- 빠른 개발이 중요하고, ORM 학습/설정 비용을 피하고 싶은 경우

## JPA 연관관계 정리
### JPA 연관관계 설정 핵심 규칙
1. 연관관계의 주인 (가장 중요)
- DB 기준으로 판단
- FK를 가진 쪽 = 연관관계 주인(자식 엔티티)
- 연관관계 수정 시 주인만 INSERT / UPDATE 반영(부모 테이블은 해당 작업과 관련이 없다.)
- 주인이 아닌 쪽(부모 엔티티)에 mappedBy 설정
- mappedBy 쪽은 읽기 전용

2. Fetch 전략 (절대 규칙)
- 기본: 전부 LAZY
- @ManyToOne → 기본 EAGER ❌ → LAZY로 강제: @ManyToOne(fetch = FetchType.LAZY)
- @OneToOne → 기본 EAGER ❌ → LAZY로 강제: @OneToOne(fetch = FetchType.LAZY)
- @OneToMany → 기본 LAZY (유지)
- @ManyToMany → 기본 LAZY (하지만 거의 안 씀)

3. ManyToOne / OneToMany (핵심 패턴)
- 연관관계 주인 & mappedBy
- ManyToOne  → 연관관계 주인 (mappedBy ❌)
- OneToMany  → 주인 아님 (mappedBy ⭕)
- 이유
    - FK는 항상 Many 쪽 테이블에 존재
    - 따라서 ManyToOne이 주인
    - 예시
        ```java
        // 연관관계 주인
        @ManyToOne(fetch = LAZY)
        @JoinColumn(name = "member_id")
        private Member member;
        ```
        
        ```java 주인 아님
        @OneToMany(mappedBy = "member")
        private List<Order> orders = new ArrayList<>();
        ```
        - OneToMany에 mappedBy 없으면 → INSERT 후 UPDATE 추가 발생 (비효율)

4. OneToMany 단독 사용 (비추천)
- FK가 반대편에 있음
- 관리 주체 불명확
- update 쿼리 자동 발생
  → ManyToOne + 양방향으로 대체

5. OneToOne
- FK 가진 쪽이 주인
- 반대편에 mappedBy
- 무조건 LAZY
- 대부분 ManyToOne(unique) 로 대체 가능
    ```java
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    ```

6. ManyToMany
- 실무 사용 금지
- 중간 테이블에 컬럼 추가 불가
    → 중간 엔티티 분리

7. 양방향 연관관계
- 편의 메서드 필수
- 한쪽만 설정하면 객체 그래프 깨짐
    ```java
    public void addOrder(Order order) {
        orders.add(order);
        order.setMember(this);
    }
    ```

8. Cascade
- 부모(Entity)에서 발생한 persist, merge, remove 등 영속성 상태 변화를 자식(Entity)에 전파
- 라이프사이클 완전히 동일할 때만 사용하고 CascadeType.ALL 남용 ❌

9. OrphanRemoval
- 컬렉션에서 제거 자식 엔티티 제거 시 자식 데이터가 DB에서 DELETE된다.
- 부모가 완전 100% 소유할 때만 사용한다.
