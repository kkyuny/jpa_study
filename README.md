## Ch03. QueryMethod 살펴보기
#### 1. IL
- jpa에서 사용하는 QueryMethod들을 배웠다.
    
#### 2. 트러블슈팅
- JPA/ could not initialize proxy - no Session
  - ㅁㄴㅇㄹ 
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
#### 3. 추가 정리 
- getOne, findById
- 롬복 컨스트럭트 어노테이션
