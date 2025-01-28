## Ch06. 연관관계

### 1. IL
#### 테이블 간의 연관관계
- JPA에서 테이블 간의 **1:N, N:1** 관계를 매핑하는 방법을 학습.
- **현업에서 1:N 관계 대신 1:1 관계를 사용하는 경우**
  - 주문이나 결제와 같이 **서비스 중단을 방지해야 하는 테이블**은 1:N보다 1:1 관계로 설계하는 경우가 있음.
- 추가 정리
  - 1:N 관계에서의 문제점
    - N개 자식 데이터의 의존성 증가    
      - 데이터베이스 조인이 많아질수록 성능에 부담을 주고, 한쪽 테이블의 데이터 누락이나 무결성 문제가 발생하면 서비스가 중단될 위험이 커질 수 있음.
    - 잠금 문제
      - 다수의 자식 데이터를 관리하다 보면, 동시에 여러 트랜잭션이 발생해 **데드락(Deadlock)**이나 테이블 잠금이 발생할 가능성이 높아질 수 있음.
  
  - 1:1 관계로 설계했을 때의 장점
    - 1:1 관계는 한 번에 하나의 연관된 데이터를 조회하거나 관리할 수 있어 성능에 안정적
    - 조인 처리 비용이 적어지고, 데이터의 조회/갱신이 단순화되어 트랜잭션 관리에 용이
    - 한 엔티티가 다른 엔티티의 확장 정보를 저장하는 형태로 설계하므로, 필요한 데이터를 독립적으로 처리할 수 있음.


#### 매핑 설정 코드
```java
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne
@ToString.Exclude
private Users user;
```
- @JoinClumn
  - 지정된 컬럼("user_id")를 통해 테이블 간의 **JOIN 조건**을 설정
  - insertable, updatable: 컬럼 값의 읽기,쓰기 조건 설정
  - new ArrayList<>(): 초기화를 통해 `NullPointerException`을 방지한다.
- @ToString.Exclude: 양방향 매핑 시, toString() 호출로 인한 무한 순환참조를 방지
- FetchType 설정
  - EAGER: 엔티티를 조회할 때 연관된 데이터 즉시 로드 및 객체를 영속적으로 사용한다는 의미
- LAZY: 연관된 데이터는 실제 필요할 때 로드(지연로딩 방식)

### 2. 트러블슈팅
#### 1) java: constructor () is already defined in class lombok
- 원인: 롬복 사용 시, 동일한 생성자를 여러번 정의했을 가능성이 높음.
- 해결
  - `@NoArgsConstructor`와 `@AllArgsConstructor`의 조합을 확인하여 중복선언을 제거
  - 필요 시, 특정 생성자에 `@RequiredArgsConstructor`를 활용하여 필요한 필드만 초기화
#### 2) FetchType 설정 차이로 인한 문제
- 현상: userHistory를 EAGER, reviews를 LAZY로 설정한 경우 에러 발생
- 원인
  - EAGER와 LAZY가 함께 설정될 때, **N+1 문제 또는 프록시 객체 초기화 문제** 발생 가능.
  - EZGER 로딩 시 여러 객체를 즉시 로드하려고 시도하다가 충돌.
- 해결
  - 일관된 FetchType 설정
    - 양쪽 관계를 모두 같은 방식으로 설정한다.
    - 일반적으로 양쪽 관계 설정 시 **LAZY 방식을 추천**하고 **EAGER 방식은 비추천**한다.
```
@Entity
public class Parent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Child> children = new ArrayList<>();
}

@Entity
public class Child {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Parent parent;
}
```
#### N+1 문제 발생 상황
```
List<Parent> parents = parentRepository.findAll();
for (Parent parent : parents) {
    parent.getChildren().size(); // 추가 쿼리가 실행됨
}
```
#### 실행되는 SQL
```
부모 데이터를 조회
SELECT * FROM parent;

각 부모에 대한 자식 데이터를 조회 (부모 수만큼 반복)
SELECT * FROM child WHERE parent_id = ?;
```
- 부모가 10개라면 1번의 부모 조회 쿼리 + 10번의 자식 조회 처리 실행으로 총 11번의 쿼리가 실행된다.
- 하지만 Fetch Join을 사용한다면 쿼리 실행횟수가 1회로 줄어든다.
- 따라서 `EAGER`로 선언 시 서로를 즉시 로드하려는 시도가 반복 되어 추가적인 쿼리 실행 가능성이 높아 `N+1 문제` 발생 및 항상 연관된 엔티티를 즉시 로드하기 때문에 불필요한 데이터가 로드될 수 있다.
- 이를 위해 일반적인 양방향 연관관계에서는 `LAZY` 또는 `Fetch Join`을 사용한다.




