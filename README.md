## Ch08. Cascade(영속성 전이) 활용하기
#### 1. IL
- Cascade
- Cascade는 6개의 타입이 있다. 기본 값은 아무것도 설정되어 있지 않은 상태이다.
  - ALL: 
  - PERSIST: PERSIST가 일어날 때 연관이 되어있는 엔티티에도 PERSIST를 진행해라.
  - MERGE: MERGE가 일어날 때 ~~
  - DETACH: 영속성 전이를 하지 않겠다.
  - REMOVE:
  - REFRESH:
- JPA의 no session
```
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne()
@ToString.Exclude
private Users user;
``` 