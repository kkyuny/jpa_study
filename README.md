### Ch02. 기본적인 JPA 메서드 학습

#### 1. IL  
- JPA에서 기본적인 CRUD 메서드를 학습함.  
- MyBatis 대비 JPA가 확실히 더 편리한 점을 확인함.  

---

#### 2. 트러블슈팅  
- **문제점**: `yml` 설정에서 `data.sql`과 `schema.sql`의 쿼리문이 적용되지 않음.
- **발생 상황**:
  - 강의에서는 `schema.sql` 파일 없이도 `data.sql`의 `INSERT`문이 정상적으로 작동.
  - 개인 환경에서는 `data.sql` 쿼리가 동작하지 않음.
- **해결 과정**:
  - `schema.sql`로 테이블을 생성했으나, 여전히 `data.sql`이 실행되지 않음.
  - `ddl-auto: none` 설정 추가 후 문제 해결:
    ```yaml
    jpa:
      hibernate:
        ddl-auto: none
      show-sql: true
      properties:
        hibernate:
          show_sql: true
          format_sql: true
    ```
  - `ddl-auto: none` 설정으로 Hibernate가 테이블 생성/수정을 하지 않게 하니 `data.sql`과 `schema.sql`이 정상 작동 및 테스트 코드 통과.
- **Hibernate란?**: 자바에서 사용하는 ORM 프레임워크중 하나로 Hibernate 기반으로 만들어진 ORM 기술 표준이 JPA다.
- **ddl-auto 옵션 정리**
  - **none**: Hibernate가 데이터베이스와 관련된 작업을 수행하지 않음 (테이블 생성/수정 미지원).
  - **update**: 기존 테이블 유지, 엔티티 변경 시 스키마 업데이트.
  - **create**: 애플리케이션 시작 시 기존 테이블 삭제 후 새 테이블 생성.
  - **create-drop**: 애플리케이션 종료 시 테이블 삭제 (시작 시 생성, 종료 시 삭제).
  - **validate**: 테이블 존재 여부와 매핑 검증 (변경/생성 미지원).

---

#### 3. 추가 정리
- **롬복(@ArgsConstructor) 어노테이션**
  - 편리하지만 무분별한 사용을 방지하기 위해 주요 기능을 정리
    - `@NoArgsConstructor`: 파라미터 없는 디폴트 생성자 생성.
    - `@AllArgsConstructor`: 모든 필드를 파라미터로 받는 생성자 생성.
    - `@RequiredArgsConstructor`: `final`이나 `@NonNull` 필드만 파라미터로 받는 생성자 생성.
  - **활용 팁**
    - 특정 필드만 초기화해야 하는 경우 `@NonNull`을 적절히 활용.
=======
## Ch09. 커스텀 쿼리 사용하기
#### 1. IL
- 커스컴 쿼리를 사용하는 이유
  - where절이 복잡해서 코드 가독성이 떨어지는 경우가 발생할 때 메서드 이름을 재정의해서 사용한다.
  - 엔티티에 연결되지 않은 컬럼의 쿼리 사용이 필요할 때 사용한다.
- 커스텀 쿼리 사용방법
  - JPA의 엔티티를 기반으로 쿼리문을 작성한다면 JPQL이라고 보통 부르며, @Query(value = "사용할 쿼리 입력")과 같은 형태로 사용한다.
  - 컬럼의 이름은 엔티티와 동일하게 사용하며, 파라미터는 ?1과 같은 형태(순서대로 입력 필요함)와 
  - :컬럼명(메서드의 파라미터 부분에 @Param("컬럼명"))과 같이 name을 지정한 형태를 사용한다.
- @Column(columnDefinition= ) 사용 시 주의점
  - 컬럼 데피니션을 사용 시 sql type을 명시하지 않으면 타입이 삭제된다.
  - 또한 값을 설정 시 nullable과 같은 다른 옵션과 호환되서 사용되는 것이 아니고
  - 각각의 옵션이 이어져서 선언이 되기 때문에 사용에 주의가 필요하다.
  - ex)@Column(columnDefinition="datetime(6) default null", nullable = false)
    - 위와 같이 선언 시 ddl문에 null과 not null의 옵션이 이어져서 선언될 수 있다.
- Native Query 활용하기
  - @Query(value = "사용할 쿼리")에서 nativeQuery = true만 추가하면 된다.
  - 네이티브 쿼리는 특정한 JPA의 설정과 엔티티의 속성을 사용하지 못하기 때문에 실제 DB테이블과 동일한 컬럼명과 테이블명을 사용해야한다.
  - Show databases와 같은 JPA 형태로는 실행할 수 없는 쿼리를 네이티브 쿼리를 사용해서 실행한다.
  - 네이티브 쿼리는 update와 같은 특정 DML문의 성능향상을 위해서 사용하는 경우가 있다.
- Convert 사용하기
  - 네이티브 쿼리를 통해 얻어온 값을 변환할 때 @Convert를 사용한다.
  - 이 때 컨버터를 구현하기 위해서 AttributeConverter라는 인터페이스를 상속받아
  - convertToDatabaseColumn과 convertToEntityAttribues라는 메서드를 구현해서 사용한다.
  - convertToDatabaseColumn를 구현하지 않으면 영속성 컨텍스트에 의해서 해당 데이터가 유실 될 수 있다.
  - 자주 사용하는 Convert는 autoApply = true 옵션을 사용하면 편리한 점이 있을 수 있다.
