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
- UserHistory 엔티티에서 @EntityListeners(value = {MyEntityListener.class, UserEntityListener.class})
- 와 같이 위 리스너를 value로 등록하였고, 테스트 코드를 수행하면서 리스너가 동작할 때 Object o를 Users 엔티티로 cast하지 못했기 때문이다.
- 
#### 3. 추가 정리 
- 엔티티 리스너가 빈 주입이 되지 않는 이유
  - @EntityListeners(value = {UserEntityListener.class})와 같이 엔티티 리스너로 등록된 클래스는
  - 간단히 말하면 Spring IOC 컨테이너의 관리대상이 아니기 때문이다.
  - 그렇기 때문에 아래와 같이 빈을 직접 주입하는 코드를 만들어 빈을 주입하도록 해야한다.
```
@Component
public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtils.applicationContext = applicationContext;
    }

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