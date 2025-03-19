# Ch10. 임베디드 타입 활용하기

## 1. 임베디드 타입(Embedded Type)이란?
- **객체지향적인 설계를 위해 값 타입(VO, Value Object) 을 하나의 클래스로 묶어 엔티티에 포함시키는 방식**.
- 데이터베이스 테이블에서는 **여러 개의 컬럼이 되지만, JPA에서는 하나의 객체로 다룰 수 있음**.
- 재사용성이 높아지고, 관련된 데이터들을 묶어서 관리할 수 있음.

## 2. 임베디드 타입의 주요 애노테이션

### **2.1 `@Embeddable`**
- **임베디드 타입(내장 값 타입) 정의** 시 사용.
- 해당 클래스는 다른 엔티티에 포함될 수 있음.

```
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
```

### 2.2 @Embedded
- 임베디드 타입을 엔티티에 포함할 때 사용.
```
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;
}
```
- Address는 User 엔티티에 포함되어 하나의 테이블에 컬럼으로 매핑됨.
- 테이블 구조
```
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    zipcode VARCHAR(255)
);
```

### 2.3 @AttributeOverrides 및 @AttributeOverride
- 같은 @Embeddable 타입을 하나의 엔티티에서 여러 번 사용할 경우 컬럼명을 재정의해야 함.
```
@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "shipping_zipcode"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
        @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "billing_zipcode"))
    })
    private Address billingAddress;
}
```
- 테이블 구조
```
CREATE TABLE order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipping_city VARCHAR(255),
    shipping_street VARCHAR(255),
    shipping_zipcode VARCHAR(255),
    billing_city VARCHAR(255),
    billing_street VARCHAR(255),
    billing_zipcode VARCHAR(255)
);
```

## 3. @Embedded 객체가 null로 저장될 경우
- @Embedded 필드가 null이면, JPA는 해당 필드와 관련된 컬럼 값을 NULL로 저장.
- 단, 컬럼 자체가 NULL을 허용하지 않는 경우(nullable = false) 예외 발생.
```
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "city", nullable = false))
    })
    private Address address;
}
```
- **예외 발생 가능** (address가 null이면 city 필드도 null이 되기 때문):
```
Caused by: javax.persistence.PersistenceException: Column 'city' cannot be null
```
- 해결 방법
  - 객체를 null이 아닌 빈 값으로 설정 (선호)
```
user.setAddress(new Address()); // 빈 객체 저장
```
  - 컬럼을 nullable = true로 설정
```
@Column(name = "city", nullable = true)
```

## 4. 임베디드 타입 활용 시 주의할 점

- **임베디드 타입은 `@Entity`가 아님** → 식별자(`@Id`)를 가질 수 없음.
- **임베디드 타입을 `null`로 저장하면 내부 필드도 `NULL`로 저장됨.**
- **같은 `@Embeddable` 타입을 여러 번 사용할 경우 `@AttributeOverride`를 필수적으로 적용해야 함.**
- **데이터 변경 시 값 타입이므로 엔티티와 달리 `setter` 사용을 지양하고 새로운 객체로 변경하는 것이 좋음.**

## 5. 정리

| 기능 | 설명 |
|------|------------------------------------------|
| `@Embeddable` | 값 타입을 정의하는 클래스에 적용 |
| `@Embedded` | 엔티티에서 값 타입을 포함할 때 사용 |
| `@AttributeOverrides` | 동일한 임베디드 타입을 여러 번 사용할 때 컬럼명 변경 |
| `null` 처리 | `@Embedded` 필드가 `null`이면 내부 컬럼도 `null`로 저장됨 |
| 주의사항 | 값 타입은 `@Entity`가 아니므로 `@Id` 사용 불가, 변경 시 새로운 객체 할당이 권장됨 |
