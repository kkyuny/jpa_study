## Ch06. 연관관계

### 1. IL (Insights Learned)
#### 테이블 간의 연관관계
- JPA에서 테이블 간의 **1:N, N:1** 관계를 매핑하는 방법을 학습.
- **현업에서 1:N 관계 대신 1:1 관계를 사용하는 경우**
  - 주문이나 결제와 같이 **서비스 중단을 방지해야 하는 테이블**은 1:N보다 1:1 관계로 설계하는 경우가 있음.

#### 매핑 설정 코드
```java
@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private List<UserHistory> userHistoryList = new ArrayList<>();

@ManyToOne
@ToString.Exclude
private Users user;
```
