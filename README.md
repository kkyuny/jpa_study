## Ch07. 영속성이란?
#### 1. IL
- 기존에 h2 database에서 데이터의 영속성을 위해 실제 database를 연동하는 과정을 배웠다.
- 영속성 컨텍스트: JPA에서 엔티티를 감시/관리하여 데이터를 영속화하는데 사용하는 컨테이너이다.
- EntityManager: 영속성 컨텍스트의 가장 주체적인 역할을 하는 class
- 설정은 META-INF 하위에 persistence.xml을 생성하여 사용할 수 있다.
- 스프링부트에서의 기본적인 설정은 spring-boot-starter-data-jpa에서 persistence 컨텍스트에 대한 설정을 하게 된다.
- dialect
- hibernate: ddl-auto, generate-ddl 옵션의 차이
  - generate-ddl은 jpa의 구현체와 상관없이 사용할 수 있는 범용적인 옵션이다.
  - ddl-auto는 hibernate에서 제공하는 조금 더 세밀한 옵션이다.
- datasource: initialiozation-mode: 
- 충돌하는 옵션에 대한 우선순위

- Entity 캐시
  - JPA의 1차 캐시 내에 해당 entity에 대한 id 데이터가 있다면 DB에 쿼리를 날리지 않고 entity를 제공한다. 
  - 직접 id를 통하여 값을 조회하는 경우는 드물지만, 그 외 update, delete 등 id 값을 활용한 조회가 빈번하기 때문에 성능저하를 방지한다.
  - flush: 영속성 컨텍스트에 쌓여있는 데이터를 개발자가 원하는 시간에 flush() 메서드를 사용하여 의도적으로 DB에 영속화시킬 수 있다.
  - 기본적으로 Transaction이 종료되고 커밋 될 때 오토플러시가 발생한다.
  - 복잡한 로직에서 데이터 조회 시 엔티티 매니져의 데이터를 조회할 지 DB의 데이터의 값의 차이가 나는 시점(더티체크?)에 대해 고민해보면 좋을 것 같다.

- Entity 생애주기
  - 영속성 컨택스트 내에서 엔티티 매니져가 엔티티를 어떻게 변화시키는지 살펴보았다. 
  - 엔티티의 생애주기는 비영속상태(Transient), 영속상태(Persist), 준영속상태(Detach): 영속성 컨텍스트에 관리하지 않는 상태 merge 메서드로 다시 반영시킬 수 있다, 삭제상태 4가지가 있다.

- 트랜잭션(ACID): DB의 상태를 변환시키는 논리적인 기능의 단위 혹은 연산
  - 원자성(Atomicity): All or nothing, 부분적인 성공을 허용하지 않는다.
    - ex) 내가 송금을 하면 상대방은 송금을 받아야한다. 
  - 일관성(Consistency): 데이터는 언제나 일관적인 상태로 정합성이 맞아야한다. 
    - ex) 송금을 보낸 금액과 송금을 받은 금액은 같아야한다.
  - 독립성(Isolation): 하나의 트랜잭션은 다른 트랜잭션으로 부터 독립성을 갖는다.
    - ex) 송금이 완료되기 전까지 상대방의 잔고에 다른 작업을 허용하지 않아야한다.
  - 지속성(Durability): 데이터는 영구적으로 반영되어야 한다.

- @Transaction의 역할
  - 어노테이션이 적용된 영역 내에서 모든 쿼리가 문제 없이 실행되는 시점에 DB에 commit을 하게 된다.
  - 문제가 발생하면 명령어들이 rollback 된다.
  - 런타임 Exception이 발생한 경우에는 commit되지 않고 rollback 된다.
  - 하지만 체크드 Exception은 try-catch문으로 Exception을 핸들링 하지 않으면 rollback되지 않는다.
  - 스프링에선 TransactionAspectSupport라는 클래스의 completeTransactionAfterThrowing 메서드에서 
  - rollBackOn의 상태를 확인하여 처리가 된다.
  - 이 때 rollBackOn은 런타임 Exceoption 타입이나 Error 타입인 경우에만 롤백을 처리하고 그 외의 경우엔 커밋을 진행한다.
  - rollBackOn에 체크드 exception을 추가하여 사용하는 방법은 아래와 같다.
  - @Transactional(rollbackFor = Excepti  on.class) -> rollbackFor를 사용한다.
  - 또, 실수가 많이 일어나는 포인트로 다른 메서드에 의해 호출되는 메서드에는 @Transaction를 사용하면 트랜잭션이 일어나지 않는다.
  - 스프링 컨테이너가 bean에 접근할 때 다른 메서드에 의해 호출되는 메서드의 어노테이션을 처리하지 못하기 때문이다.

- 스프링 Transaction의 isolation과 propagtion
  - 트랜잭션의 다양한 격리와 전파 단계를 제공한다.
  - isolation: 4가지의 격리 단계가 있고 아래로 내려갈 수록 격리가 강화된다.
    - DEFAULT: 기본 격리 단계로 mysql은 REPEATABLE_READ이 적용된다.
    - READ_UNCOMMITTED: 커밋되지 않은 데이터를 읽을 수 있다. 그래서 더티리드라고도 불리며 업데이트 시 DB락 같은 문제가 발생할 수 있다.
    - READ_COMMITTED: 커밋된 데이터만 읽을 수 있다. @DynamicUpdate 같은 어노테이션을 사용하지 않아도 된다.
    - REPEATABLE_READ: 트랜잭션 내에서 같은 값을 여러번 조회하더라도 같은 값을 항상 제공한다.
    - SERIALZABLE: 커밋이 일어나지 않은 트랜잭션이 존재하면 락을 통해 웨이팅을 한다. 커밋이 되어야만 로직이 진행된다.
  - propagtion: 하나의 트랜잭션을 갖는 메서드에 다른 클래스의 메서드의 트랜잭션이 있을 때 이를 처리를 하는 것
  - 전파가 여러단계로 진행되면, 흐름의 파악이 어려우니 적절하게 사용해야한다.
    - REQUIRED: 일반적으로 디폴트의 전파상태이다. 기존에 사용하는 트랜잭션이 있다면 재활용하여 하나의 트랜잭션으로 사용된다.
    - 없다면 새로운 트랜잭션을 생성해서 사용한다.
    - REQUIRED_NEW: 트랜잭션이 있건 없건 새로운 트랜잭션을 생성하여 자체적으로 커밋과 롤백을 진행하겠다는 의미이다.
    - NESTED: 별도의 트랜잭션을 생성하는 것이 아니지만 NESTED로 설정된 트랜잭션이 실패하면 해당 트랜잭션만 롤백된다.
    - 트랜잭션의 실패로 상위의 트랜잭션에 영향을 주지 않으나, 상위의 트랜잭션의 롤백에는 영향을 받는다.
    - SUPPORT: 호출하는 쪽에서 트랜잭션이 있다면 재사용한다. 하지만 없다면 생성하지 않는다. 트랜잭션이 있다면 지원한다라는 의미이다.
    - NOT_SUPPROT: 호출하는 쪽에서 트랜잭션이 있더라도 트랜잭션을 사용하지 않는다. 트랜잭션이 있더라도 지원하지 않는다라는 의미이다.
    - MANDATORY: 이미 생성한 트랜잭션이 있어야하고 없다면 오류가 발생한다.
    - NEVER: 이미 생성한 트랜잭션이 없어야하고 있다면 오류가 발생한다.

- 클래스 scope의 트랜잭션과 메서드 영역의 트랜잭션
  - 클래스 영역: 모든 메서드에 클래스 영역에서 선언한 트랜잭션이 적용된다.
  - 메서드 영역: 클래스 영역에 트랜잭션이 선언되었더라도 메서드 영역의 트랜잭션을 우선하여 사용한다.
```
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne()
@ToString.Exclude
private Users user;
``` 