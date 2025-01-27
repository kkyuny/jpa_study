## Ch05. Listener 활용하기

### 1. Listener 개념
- JPA에서 제공하는 7가지 이벤트 리스너
  - **생성 및 수정 전후**: `@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`
  - **삭제 전후**: `@PreRemove`, `@PostRemove`
  - **조회 후**: `@PostLoad`
- 주요 특징
  - 이벤트 리스너는 엔티티의 상태 변화 시 특정 로직을 실행할 수 있도록 지원.
  - MyBatis 사용 경험자 입장에서 쿼리 실행 전후 동작을 설정해 코드 효율성을 높일 수 있음.
    - 예) 데이터 생성/수정 시 타임스탬프 자동 입력, 쿼리 수행 시 히스토리 데이터 생성.
  - 단점: 엔티티 리스너 구현 및 설정 과정에서 추가 작업 필요.

---

### 2. 트러블슈팅
#### 문제
- **에러 발생 상황**: `user` 테이블에 `insert` 전 `userHistory` 삽입 시 `java.lang.ClassCastException` 발생.
- **에러 원인**: 
  - 리스너에서 전달받은 `Object o` 객체를 `Users` 엔티티로 캐스팅하지 못함.
  - 이는 리스너가 Spring IOC 컨테이너의 관리 대상이 아니기 때문.

#### 문제 코드
```
//@Component // 엔티티 리스너는 스프링 빈을 주입받지 못한다.
public class UserEntityListener {
    @PreUpdate
    @PrePersist
    public void prePersistAndPreUpdate(Object o) {
        // 스프링 빈을 주입받지 못하기 때문에 BeanUtils를 사용해 빈을 가져옴.
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        Users user = (Users) o; // Object o를 Users로 캐스팅

        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        userHistoryRepository.save(userHistory); // UserHistory 저장
    }
}
```
#### 원인 분석
- 엔티티 리스너는 `@EntityListeners`로 등록되며, Spring Bean으로 관리되지 않음.
- 따라서 @Component를 사용할 수 없고, Spring 컨텍스트에서 빈을 직접 가져와야 함.
- 
