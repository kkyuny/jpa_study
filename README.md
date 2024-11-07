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
=======
## Ch04. Entity 기본속성 살펴보기
#### 1. IL
- 도메인 패키지에서 Entity의 여러가지 속성에 대해 공부하였다.
- id를 생성하는 4가지 관점에 대해 알게 되었다.
```
- GenerationType
1) IDENTITY: mysql, 마리아db 등에서 사용하는 전략이다. 트랜잭션이 종료되기 전에 id 값을 사전에 받아와서 id 값을 사용한다.
2) SEQUENCE: 오라클, 포스트그레 등에서 사용하는 전략이다. insert할 때 sequence를 증가시키는 쿼리가 실행된다.
3) TABLE: DB 종류에 상관없이 id 값을 관리하는 별도의 테이블을 두어 id를 추출하여 사용한다.
4) AUTO: default 값이며 각 db에 적절한 방식을 사용한다고 한다.
```
- enum에 대해서도 간략하게 배우게 되었다.
#### 2. 트러블슈팅
- enum이 사용되는 컬럼에서 @Enumerated(value = EnumType.STRING)의 설정이 없다면 결과값이 저장된 순서와 매핑된다.
이 때, enum의 저장순서가 변경되면 db에 저장된 값과 잘못된 값이 매핑되는 문제가 발생할 수 있다.
이를 위해 EnumType.STRING 설정을 하게 된다면 순서에 상관 없이 db에 저장된 값과 매핑되어 문제를 해결할 수 있다.
#### 3. 추가 정리 
- 트러블슈팅 내용 추가정리
```
  public enum Gender {
    MALE,
    FEMALE
  }
  와 같이 enum이 선언되어 있다면 실제 DB에는 MALE은 1, FEMALE은 2로 저장된다.
  여기서 
  public enum Gender {
    MALE,
    UNKNOWN,
    FEMALE
  } 
  와 같이 입력 누락 등의 이유로 중간에 새로운 항목이 추가된다라고 하면 
  기존의 FEMALE의 값이 2가 되고 UNKNOWN의 값은 1이 된다.
  하지만 DB의 값은 새로운 enum에 맞춰 변하지 않기 때문에 혼동이 발생할 수 있다.
  
  이 때 @Enumerated(value = EnumType.STRING)를 사용하게 되면
  enum의 값이 실제 DB와 동일하게 저장되기 때문에 앞서 말한 문제를 해결할 수 있다.
```
