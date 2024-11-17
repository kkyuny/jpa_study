package com.study.jpa.bookmanager.domain;

import com.study.jpa.bookmanager.domain.listener.Auditable;
import com.study.jpa.bookmanager.domain.listener.MyEntityListener;
import com.study.jpa.bookmanager.domain.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // jpa에서는 인자가 없는 생성자가 필요하기 때문에 선언한다.
@AllArgsConstructor // jpa 엔티티 선언
@Getter
@Setter
@Builder // 빌더의 형식을 갖고 생성자를 생성
@Entity
/*@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)*/
@Table(name = "users")
@ToString(callSuper = true) // 상속받는 부모 필드값의 표현을 위해서 선언이 필요하다.
@EntityListeners(value = {UserEntityListener.class})
/* 기본적으로 설정하지 않지만 table 네임의 지정 등이 필요할 때 사용한다.
    일반적으로 엔티티와 테이블의 네임은 동일하게 매핑시켜 사용한다.
    그 외 index, unique 설정을 할 수 있다.
    하지만 실제 db에 설정해서 사용하는 것이 여러가지 이유로 보편적 인 사용이다.
 */
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase
    private Long id;
    /*
        - GenerationType
            1) IDENTITY: mysql, 마리아db 등에서 사용하는 전략이다. 트랜잭션이 종료되기 전에 id 값을 사전에 받아와서 id 값을 사용한다.
            2) SEQUENCE: 오라클, 포스트그레 등에서 사용하는 전략이다. insert할 때 sequence를 증가시키는 쿼리가 실행된다.
            3) TABLE: DB 종류에 상관없이 id 값을 관리하는 별도의 테이블을 두어 id를 추출하여 사용한다.
            4) AUTO: default 값이며 각 db에 적절한 방식을 사용한다고 한다.
     */
    @Column(name = "name")
    /* 해당 컬럼의 name을 정할 수 있는 어노테이션이다.
        name에 설정한 값이 db와 실제 매핑되기 때문에 class의 필드 값과 상이하게 사용할 수 있다.
        @Column(unique = true) : 해당 컬럼이 유니크 속성을 갖는다.
     */
    private String name;
    @NonNull // 해당 컬럼이 not null 속성을 갖는다.
    private String email;
    // jpa에서 생성과 수정된 시간은 일반적으로 도메인에 항상 포함되게 되어있다.

    @OneToMany(fetch = FetchType.EAGER)
    private List<Address> address;

    @Transient // 해당 어노테이션을 추가하면 영속성 관리에서 제외되어 db에 영향을 주지 않는다.
    private String testDate;

    /* enum의 매핑을 String으로 하게 된다.
        순서로 매핑을 하게되면 enum class의 순서가 변경될 경우 잘못된 값이 매핑될 수 있기 때문에
        value = EnumType.STRING의 설정을 반드시 사용하도록 하자.
     */
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    /* 변수명에 복수형을 쓰는것이 트렌드이다.
        추가로 로직에 따라서 userHistoryList가 널포인트익셉션이 발생할 수 있기 때문에 기본형을 선언해주는 것이 좋다.
        join column을 명시하지 않으면 가상의 조인 테이블을 생성하게 된다.
        그리고 insertable, updateable 설정을 추가하여 User 테이블에서 해당 값을 조작하지 못하도록 한다.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private List<UserHistory> userHistoryList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();
}
