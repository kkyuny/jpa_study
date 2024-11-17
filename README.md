## Ch06. 연관관계
#### 1. IL
- 테이블간의 연관관계에 대해 학습하였다.
- 개념적으로 1:N를 설정해야하는 관계에서 실제 현업에서는 주문이나 결제같이 서비스가 중단되어서가 안되는 테이블의 경우에
- 1:1 관계로 테이블 설계를 해야하는 경우가 있다고 한다.
```
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne()
@ToString.Exclude
private Users user;
```
- 이와 같은 방식으로 맵핑관계를 설정하는데 @JoinColumn을 설정하면 해당 컬럼을 이용하여 join이 실행된다.
- new ArrayList<>()객체가 선언되지 않았을 때 에러를 방지하기 위하여 설정한다.
- @ToString.Exclude는 스택오버플로우 문제를 방지하기 위해 설정한다.
- EAGER 방식은 해당 객체를 영속적으로 사용하겠다는 의미이다.

#### 2. 트러블슈팅
- java: constructor () is already defined in class lombok
```

```
- UserHistory date 입력 문제
- UserHostory 객체 save 문제
- fetch = FetchType.EAGER, LAZY 방식에 따른 차이점
```
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@OneToMany
@JoinColumn(name = "user_id")
@ToString.Exclude
private List<Review> reviews = new ArrayList<>();
- 위 엔티티 코드에서 userHistoryList를 Lazy 방식으로 reviews를 Eager 방식을 사용하면 에러가 발생한다.
```

#### 3. 추가 정리
- @Data 어노테이션
- 