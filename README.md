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