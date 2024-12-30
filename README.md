## Ch09. 커스텀 쿼리 사용하기
#### 1. IL
- 커스컴 쿼리를 사용하는 이유:
- @Column(columnDefinition= ) 사용 시 주의점
  - 컬럼 데피니션을 사용 시 sql type을 명시하지 않으면 타입이 삭제된다.
  - 또한 값을 설정 시 nullable과 같은 다른 옵션과 호환되서 사용되는 것이 아니고
  - 각각의 옵션이 이어져서 선언이 되기 때문에 사용에 주의가 필요하다.
  - ex)@Column(columnDefinition="datetime(6) default null", nullable = false)
    - 위와 같이 선언 시 ddl문에 null과 not null의 옵션이 이어져서 선언될 수 있다.