# Ch09. 커스텀 쿼리 사용하기

## 1. 커스텀 쿼리를 사용하는 이유
- **메서드 네이밍 기반 쿼리의 한계**
  - `findByColumnAndStatus()`와 같은 메서드 네이밍 기반의 쿼리는 조건이 복잡해질 경우 가독성이 떨어짐.
  - 복잡한 `WHERE` 절을 처리하기 위해 **JPQL 또는 네이티브 쿼리를 활용**.

- **엔티티와 연결되지 않은 컬럼 사용**
  - JPA는 엔티티를 기반으로 자동으로 SQL을 생성하지만, **엔티티에 없는 컬럼을 사용할 경우 직접 쿼리 작성이 필요**.
  - 예: DB 함수 호출, 서브쿼리 활용, 그룹핑된 데이터 조회 등.

## 2. 커스텀 쿼리 사용 방법

### **2.1 JPQL (Java Persistence Query Language)**
- **JPQL은 엔티티 객체를 대상으로 하는 쿼리**. (SQL과 유사하지만, 테이블이 아니라 엔티티를 기준으로 작성)
- `@Query` 어노테이션을 사용하여 작성.

```java
@Query("SELECT u FROM User u WHERE u.status = :status")
List<User> findByStatus(@Param("status") String status);
```
- JPQL의 특징
  - 컬럼명이 아닌 엔티티 필드명 사용 → User 엔티티의 status 필드를 참조.
- 파라미터 사용 방법
  - 순서 기반 (?1)
```
@Query("SELECT u FROM User u WHERE u.status = ?1")
List<User> findByStatus(String status);
```
  - 이름 기반 (:파라미터명)
```
@Query("SELECT u FROM User u WHERE u.status = :status")
List<User> findByStatus(@Param("status") String status);
```

## 3. 네이티브 쿼리 활용하기
- JPA가 자동 생성하는 SQL이 아닌 직접 SQL을 실행해야 하는 경우 `nativeQuery = true` 옵션을 사용.

### 3.1 네이티브 쿼리 사용 예제
```
@Query(value = "SELECT * FROM users WHERE status = :status", nativeQuery = true)
List<User> findByStatusNative(@Param("status") String status);
```
- 특징
  - JPA 엔티티와 매핑된 컬럼명을 사용할 수 없음 → 실제 DB 컬럼명과 테이블명을 사용해야 함.
  - JPQL에서는 불가능한 SQL 기능을 사용 가능
    - `SHOW DATABASES;`
    - `UNION`, `WITH RECURSIVE` 등.
      
### 3.2 네이티브 쿼리의 장점
- 복잡한 SQL 실행 가능 (특히 `JOIN`, `GROUP BY`, `HAVING` 등)
- 성능 최적화 가능 (인덱스 힌트, DB 특정 함수 활용 등)

### 3.3 네이티브 쿼리 사용 시 주의점
- JPA의 자동 관리 기능 (영속성 컨텍스트, 캐시 등)을 활용할 수 없음.
- 결과를 엔티티에 직접 매핑하려면 `@SqlResultSetMapping` 또는 DTO 사용 필요.

## 4. Convert 사용하기 (@Convert)
- JPA에서는 DB 저장 시 변환이 필요한 경우 `@Convert`를 활용.

### 4.1 @Convert 사용 예제
```
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    @Convert(converter = StatusConverter.class)
    private UserStatus status;
}
```

```
@Converter(autoApply = true) // 모든 UserStatus 타입에 자동 적용
public class StatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus status) {
        return status.name(); // Enum → String 변환
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        return UserStatus.valueOf(dbData); // String → Enum 변환
    }
}
```

### 4.2 Convert 사용 이유
- DB에서 Enum을 문자열로 저장할 때 변환 필요
- Boolean, JSON, 암호화된 데이터 변환 가능
- `autoApply = true`를 사용하면 동일한 타입을 가진 모든 필드에 자동 적용 가능.

## 5. 정리

| 기능 | JPQL | 네이티브 쿼리 |
|------|------|--------------|
| **사용 방식** | `@Query("SELECT u FROM User u WHERE u.status = :status")` | `@Query(value = "SELECT * FROM users WHERE status = :status", nativeQuery = true)` |
| **대상** | 엔티티 객체 기준 (`User`) | 실제 DB 테이블 (`users`) |
| **컬럼명** | 엔티티 필드명 사용 (`status`) | DB 컬럼명 사용 (`status`) |
| **JPQL 문법 지원** | ✅ 지원 | ❌ 미지원 |
| **DB 특정 기능 사용 가능** | ❌ 제한적 | ✅ 가능 (예: `SHOW DATABASES`) |
| **성능 최적화** | ❌ 자동 최적화 제한 | ✅ 인덱스 힌트 활용 가능 |
| **영속성 컨텍스트 적용** | ✅ 적용됨 | ❌ 적용 안됨 |

## 6. 결론
- **JPQL은 JPA 엔티티를 기준으로 작성되며, 네이티브 쿼리보다 코드 유지보수가 용이함.**
- **네이티브 쿼리는 JPA의 자동 관리 기능을 사용할 수 없지만, 복잡한 SQL 실행 및 성능 최적화가 가능.**
- **DB 저장 시 변환이 필요할 경우 `@Convert`를 활용하여 데이터를 변환할 수 있음.**
