## Ch07. 영속성이란?
#### 1. IL
- 기존에 h2 database에서 데이터의 영속성을 위해 실제 database를 연동하는 과정을 배웠다.
- 영속성 컨텍스트: JPA에서 데이터를 영속화하는데 사용하는 컨테이너이다.
- EntityManager: 영속성 컨텍스트의 가장 주체적인 역할을 하는 class
- 설정은 META-INF 하위에 persistence.xml을 생성하여 사용할 수 있다.
- 스프링부트에서의 기본적인 설정은 spring-boot-starter-data-jpa에서 persistence 컨텍스트에 대한 설정을 하게 된다.
- dialect
- hibernate: ddl-auto, generate-ddl 옵션의 차이
  - generate-ddl은 jpa의 구현체와 상관없이 사용할 수 있는 범용적인 옵션이다.
  - ddl-auto는 hibernate에서 제공하는 조금 더 세밀한 옵션이다.
- datasource: initialiozation-mode: 
- 충돌하는 옵션에 대한 우선순위
```
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne()
@ToString.Exclude
private Users user;
``` 