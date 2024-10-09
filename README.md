## Ch02. 기본적인 jpa 메서드 학습
#### 1. IL
- jpa에서 사용하는 기본적인 crud 메서드들을 배웠다.
- 기본적인 메서드들을 사용해보니 mybatis 보다는 확실히 편리한 점이 있었다.    
#### 2. 트러블슈팅
- yml 설정에서 data.sql과 schema.sql의 쿼리문 적용에 문제가 있었다.
```
jpa:
  hibernate:
    ddl-auto: none
  show-sql: true
  properties:
    hibernate:
      show_sql: true
      format_sql: true
```
강의에서는 schema.sql 파일도 생성하지 않았고 jpa의 ddl-auto 설정이 없었지만 data.sql의 insert문이 정상 동작하였다.
하지만 내 환경에서는 동작하지 않아서 schema.sql 파일에서 테이블을 create 해보았지만 data.sql 쿼리문이 동작하지 않았다.
결국 Hibernate가 테이블을 생성하거나 수정하지 않는 ddl-auto: none 설정을 추가하니 data.sql과 schema.sql이 정상 작동하고 테스트 코드를 통과할 수 있었다.

아래는 ddl-auto의 각각의 option이다. schema.sql 없이 create로 설정했을 때 코드가 동작하는지 확인해봐야겠다.
- none: Hibernate가 데이터베이스와 관련된 어떤 작업도 수행하지 않음. 즉, 테이블을 생성하거나 수정하지 않음.
- update: 기존 테이블은 유지하면서, 엔티티의 변경 사항이 있을 때만 스키마를 업데이트함. (테이블을 삭제하지 않음)
- create: 애플리케이션이 시작될 때 기존 테이블을 모두 삭제하고, 엔티티에 맞춰 새로운 테이블을 생성함.
- create-drop: 애플리케이션이 종료될 때 테이블을 삭제함. (테이블은 애플리케이션 시작 시 생성되고, 종료 시 삭제됨)
- validate: 테이블이 존재하는지와 엔티티 매핑이 일치하는지 검증만 하고, 변경이나 생성은 하지 않음.
#### 3. 추가 정리 
- getOne, findById
- 롬복 컨스트럭트 어노테이션
