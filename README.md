# Ch09. 커스텀 쿼리 사용하기

## 1. 커스텀 쿼리를 사용하는 이유
- **메서드 네이밍 기반 쿼리의 한계**
  - `findByColumnAndStatus()`와 같은 메서드 네이밍 기반의 쿼리는 조건이 복잡해질 경우 가독성이 떨어짐.
  - 복잡한 `WHERE` 절을 처리하기 위해 **JPQL 또는 네이티브 쿼리를 활용**.

- **엔티티와 연결되지 않은 컬럼 사용**
  - JPA는 엔티티를 기반으로 자동으로 SQL을 생성하지만, **엔티티에 없는 컬럼을 사용할 경우 직접 쿼리 작성이 필요**.
  - 예: DB 함수 호출, 서브쿼리 활용, 그룹핑된 데이터 조회 등.

---

## 2. 커스텀 쿼리 사용 방법

### **2.1 JPQL (Java Persistence Query Language)**
- **JPQL은 엔티티 객체를 대상으로 하는 쿼리**. (SQL과 유사하지만, 테이블이 아니라 엔티티를 기준으로 작성)
- `@Query` 어노테이션을 사용하여 작성.

```java
@Query("SELECT u FROM User u WHERE u.status = :status")
List<User> findByStatus(@Param("status") String status);
