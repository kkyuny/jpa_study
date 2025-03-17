# Ch08. Cascade(영속성 전이) 활용하기

## 1. IL

### Cascade 개념
- Cascade: 다른 엔티티와 관계 설정 시 `@OneToMany(cascade = CascadeType.ALL)` 등의 형태로 선언하여 사용(배열[] 형태로도 입력이 가능하다.)
- Cascade에는 6개의 타입이 존재하며, 기본값은 설정되지 않은 상태

### Cascade Type 종류
- **ALL**: 모든 Cascade 옵션을 적용
- **PERSIST**: `PERSIST`가 발생할 때 연관된 엔티티에도 `PERSIST` 적용
- **MERGE**: `MERGE`가 발생할 때 연관된 엔티티에도 `MERGE` 적용
- **DETACH**: 부모 엔티티가 `DETACH`될 때 연관된 엔티티도 `DETACH` 상태로 변경
- **REMOVE**: 부모 엔티티가 삭제될 때 연관된 엔티티도 삭제
  - 단, 연관된 엔티티가 다른 곳에서 참조되고 있다면 삭제되지 않을 수 있음
- **REFRESH**: 부모 엔티티가 `REFRESH`될 때 연관된 엔티티도 `REFRESH` 적용

---

### 연관관계 제거
- `REMOVE`를 이용하여 부모 엔티티를 삭제하면, 연관된 자식 엔티티도 삭제될 수 있지만, **연관관계 필드가 자동으로 `null`로 설정되지는 않음**  
  - 자식 엔티티가 다른 곳에서 참조되지 않는다면 함께 삭제됨  
  - 만약 외래 키(FK) 제약 조건이 걸려 있다면 삭제 시 오류 발생 가능  

- 연관된 엔티티를 실제로 DB에서 삭제하려면 `@OneToMany(orphanRemoval = true)` 옵션을 사용  
  - **orphanRemoval = true**: 부모와의 연관관계가 제거되면, **고아 객체(Orphan Object)** 가 되어 DB에서도 자동 삭제됨  
  - **cascade = REMOVE**와의 차이점:  
    - `cascade = REMOVE`: 부모 엔티티가 삭제될 때 자식도 삭제  
    - `orphanRemoval = true`: 부모와의 관계가 끊어지면(부모가 삭제되지 않더라도) 자식이 삭제됨 

#### **REMOVE vs orphanRemoval 차이**
| 옵션 | 동작 방식 |
|------|----------|
| `cascade = CascadeType.REMOVE` | 부모 엔티티 삭제 시 연관된 엔티티도 삭제되지만, 다른 엔티티가 참조 중이면 삭제되지 않을 수 있음 |
| `orphanRemoval = true` | 부모 엔티티와의 연관관계가 제거되면, 연관된 엔티티가 고아 객체가 되어 자동 삭제됨 |

---

### 소프트 딜리트 (Soft Delete)
- 실제 운영 환경에서는 `DELETE`로 데이터를 제거하는 경우가 드물다.
- 대신 `boolean deleted` 같은 컬럼을 추가하여 삭제 여부를 관리
- 삭제된 데이터를 제외하고 조회하는 방법:
  - `findAllByDeletedFalse()` 메서드 활용
  - `@Where(clause = "deleted = false")` 어노테이션을 엔티티에 추가

```java
@Entity
@Where(clause = "deleted = false")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private boolean deleted = false;
}

--- 

### JPA의 No Session 문제
- Lazy Loading을 사용하는 경우, 영속성 컨텍스트(Session)가 닫힌 후 프록시 객체를 조회하려고 하면 LazyInitializationException이 발생
- 해결 방법
  - FetchType을 EAGER로 변경 (비효율적이므로 권장하지 않음)
  - @Transactional을 사용하여 서비스 계층에서 트랜잭션을 유지
  - JOIN FETCH를 사용하여 미리 데이터를 로딩
```
// JOIN FETCH를 사용한 해결 방법
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

### `JOIN` VS `FETCH JOIN`
