## Ch03. QueryMethod 살펴보기
#### 1. IL
- jpa에서 사용하는 QueryMethod들을 배웠다.
    
#### 2. 트러블슈팅
- JPA/ could not initialize proxy - no Session
  - JPA를 사용하여 DB에 저장 된 리소스를 불러와 반환하는 경우, 값의 생명주기를 결정하는 방식은 lazy와 eager 방식이 있다.
  - lazy는 컨트롤러 -> 서비스 -> 레포지토리를 통해서 값을 반환받을 때, 값을 초기화하지 않고 proxy 객체에 정보를 채우기 때문에 반환된 값을 컨트롤러에서 사용을 하게 되면 에러가 발생한다.
  - 반환 된 값(영속성 컨텍스트)은 서비스단에서 트랜잭션과 동일한 생명주기를 갖기 때문에 컨트롤러로 값이 반환되는 순간 영속성 상태가 끝나게되서 에러가 발생한다.
  - 해결방법
    - lazy 방식이 아닌 eager 방식(즉시로딩)을 사용(비추): 값을 가져올 때 proxy 객체에 정보를 채우는 것이 아닌 해당 객체에 바로 정보를 채워 영속성 이슈를 없앤다.
    - 반환 된 값을 컨트롤러에서 사용하는 것이 아닌 서비스단에서 해당 값을 처리(추천): 영속성이 보장되는 서비스단에서 필요에 맞게 해당 값을 처리하여 영속성 이슈를 발생시키지 않는다.
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
