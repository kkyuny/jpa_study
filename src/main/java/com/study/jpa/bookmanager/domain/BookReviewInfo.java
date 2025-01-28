package com.study.jpa.bookmanager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@ToString(callSuper = true) // 상속받는 부모 필드값의 표현을 위해서 선언이 필요하다.
public class BookReviewInfo extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private float averageReviewScore; // 스몰타입을 사용하는 이유는 널을 허용하기 위해서 사용한다.
    private int reviewCount;

    // private Long bookId;
    /* book과 관련된 컬럼은 제거해줘야한다.
        Book을 생성시 해당 정보가 중복되기 때문이다.
        또 특별한 경우가 아니면 @OneToOne 설정은 단방향으로 한다.
        양방향으로 설정 시 순환참조 오류가 생길 수 있다.
     */
    @OneToOne(optional = false) // optional이 false면 Book을 inner join을 아니면 left join을 한다.
    private Book book; // 일반적인 오브젝트는 참조할 수 없다.
}
