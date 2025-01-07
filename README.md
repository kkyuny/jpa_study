### Ch03. QueryMethod 살펴보기

#### 1. IL
- JPA에서 사용하는 QueryMethod를 학습함.
- 다양한 네이밍의 쿼리 메서드들이 제공되며, 이는 코드 가독성을 높이기 위한 다양한 형태의 네임을 제공하기 위함.

---

#### 2. 트러블슈팅
**1) `could not initialize proxy - no Session` 에러**  
- **발생 원인**  
  - JPA는 엔티티 값을 불러올 때 `lazy`(지연 로딩)와 `eager`(즉시 로딩) 방식을 제공.  
  - `lazy` 방식에서는 컨트롤러 -> 서비스 -> 레포지토리로 값을 반환받을 때, 값을 초기화하지 않고 `proxy` 객체에 정보를 담음.  
  - `proxy` 객체는 서비스 영역에서 트랜잭션과 동일한 생명주기를 가지므로 컨트롤러로 반환되는 순간 영속성 상태가 끝남.  
  - 컨트롤러에서 반환된 값을 사용하려 할 때 `could not initialize proxy - no Session` 에러 발생.
  - lazy로 설정된 연관 엔티티는 초기에는 프록시 객체로 대체되고, 실제 데이터는 액세스가 발생할 때 로드됩니다.
  - `eager` 방식은 JPA에서 엔티티가 로드될 때 연관된 모든 엔티티를 즉시 함께 로드한다.
- **해결 방법**:  
  1. **`eager` 방식 사용 (비추천)**  
     - `proxy` 객체 대신 값을 즉시 채워 영속성 이슈를 없앰.  
     - 단, 성능 문제를 유발할 가능성 있음.  
  2. **서비스단에서 값 처리 (추천)**  
     - 영속성이 보장되는 서비스 영역에서 필요한 값을 처리.  
     - 컨트롤러로 반환 전에 데이터를 가공하여 영속성 문제 방지.  
- **Lazy(지연 로딩)와 Eager(즉시 로딩) 사용 예제**
  ```
    @Entity
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private String name;

        // @OneToMany, @ManyToMany는 기본적으로 LAZY로 설정되어 있다.
        // 연관된 데이터가 많을 수 있기 때문에 그런 것 같다.
        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // 지연 로딩 설정
        private List<Order> orders = new ArrayList<>();
    }
    
    @Entity
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private String productName;

        // @ManyToOne, @OneToOne은 기본적으로 EAGER로 설정되어 있다.
        // 연관된 데이터가 적기 때문에 그런 것 같다.
        @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩 설정
        @JoinColumn(name = "user_id")
        private User user;
    }
  ```
---

**2) @OneToMany 설정 시 테이블 자동 생성 이슈**  
- **발생 원인**:  
  - `ddl-auto: none` 설정으로 인해 JPA가 테이블 생성/수정을 수행하지 않음.  
  - 따라서 `@OneToMany` 관계에서 필요한 중간 테이블이 생성되지 않아 매핑 이슈 발생.  

- **해결 방법**:  
  - `ddl-auto` 옵션을 `update`로 변경하여 이슈 해결.
  - `@OneToMany`에서 필요한 중간 테이블을 해당 옵션을 통하여 자동으로 생성함.
  - `update`는 테이블이 없으면 생성, 엔티티와 기존 테이블의 스키마는 변경 사항만 반영.
  - ex)엔티티에는 있는 컬럼이 테이블에 없다면 컬럼을 생성하여 반영

---

#### 3. 추가 정리
- 연관 매핑 관계는 Ch.06에서 상세히 다룰 예정이므로 여기에서는 생략.
