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