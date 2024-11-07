## Ch05. Listener 활용하기
#### 1. IL
- jpa에서 사용하는 Listener를 배웠다.
```
JPA에서는 아래와 같이 7가지 이벤트 리스너 기능을 제공한다.
@PrePersist
@PreRemove
@PreUpdate
@PostPersist
@PostUpdate
@PostRemove
@PostLoad
각각의 기능은 직관적으로 잘 나와있는 것 같다.
특히 mybatis를 주로 쓰는 나의 입장에서는 쿼리문을 수행하기 전과 후의 동작을 설정하여 사용한다는 점에서
데이터의 create, update 시간의 입력, 특정 쿼리 수행 시 history create 등의 사용으로 코드 효율성이 크게 높아질 수 있을 것 같다고 생각했다.
하지만 반면으로는 이를 위해 인터페이스를 생성하고, Entity에 Listener 설정을 하는 등의 수고는 감수해야한다.
```
    
#### 2. 트러블슈팅
- java.lang.ClassCastException
  - user 테이블에 insert 전 userHistory를 insert하는 과정에서 해당 에러가 발생하였다.
  - 에러 원인은 호환되지 않는 클래스 유형을 형 변환하려고 했기 때문이다.
```
//@Component // 엔티티 리스너는 스프링 빈을 주입받지 못한다.
public class UserEntityListener {
@PreUpdate
@PrePersist
public void prePersistAndPreUpdate(Object o){
// 스프링 빈을 주입받지 못하기 때문에 BeanUtils를 생성하여 bean을 가져온다.
UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        Users user = (Users) o;

        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        userHistoryRepository.save(userHistory);
    }
}
```
none: 스키마 작업을 하지 않음. 데이터베이스 테이블은 이미 존재해야 하며, JPA가 이를 자동으로 생성하거나 수정하지 않는다.
update: 테이블이 없으면 생성하고, 기존 테이블이 있으면 변경 사항을 반영하여 업데이트한다.
create: 애플리케이션 시작 시 기존 테이블을 모두 삭제한 후 새로 생성한다.
create-drop: 애플리케이션 시작 시 테이블을 생성하고, 애플리케이션 종료 시 테이블을 삭제한다.
validate: 데이터베이스와 엔티티 간의 스키마가 일치하는지 확인만 하고, 생성이나 업데이트 작업은 하지 않는다.
```
#### 3. 추가 정리 
- 연관 매핑관계 정리하기

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}

@PreUpdate
@PrePersist
public void prePersistAndPreUpdate(Object o){
    // 스프링 빈을 주입받지 못하기 때문에 BeanUtils를 생성하여 bean을 가져온다.
    UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);
    
    ...
}
```