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
- **ddl-auto 옵션 정리**:
  - **none**: Hibernate가 데이터베이스와 관련된 작업을 수행하지 않음 (테이블 생성/수정 미지원).
  - **update**: 기존 테이블 유지, 엔티티 변경 시 스키마 업데이트.
  - **create**: 애플리케이션 시작 시 기존 테이블 삭제 후 새 테이블 생성.
  - **create-drop**: 애플리케이션 종료 시 테이블 삭제 (시작 시 생성, 종료 시 삭제).
  - **validate**: 테이블 존재 여부와 매핑 검증 (변경/생성 미지원).

---

#### 3. 추가 정리
- **롬복(@ArgsConstructor) 어노테이션**
  - 편리하지만 무분별한 사용을 방지하기 위해 주요 기능을 정리:
    - `@NoArgsConstructor`: 파라미터 없는 디폴트 생성자 생성.
    - `@AllArgsConstructor`: 모든 필드를 파라미터로 받는 생성자 생성.
    - `@RequiredArgsConstructor`: `final`이나 `@NonNull` 필드만 파라미터로 받는 생성자 생성.
  - **활용 팁**:
    - 특정 필드만 초기화해야 하는 경우 `@NonNull`을 적절히 활용.
