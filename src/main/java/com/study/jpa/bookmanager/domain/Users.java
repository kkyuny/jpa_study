package com.study.jpa.bookmanager.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor // jpa에서는 인자가 없는 생성자가 필요하기 때문에 선언한다.
@AllArgsConstructor // jpa 엔티티 선언
@Getter
@Setter
@Builder // 빌더의 형식을 갖고 생성자를 생성
@Entity
@ToString
@Table(name = "users")
/* 기본적으로 설정하지 않지만 table 네임의 지정 등이 필요할 때 사용한다.
    일반적으로 엔티티와 테이블의 네임은 동일하게 매핑시켜 사용한다.
    그 외 index, unique 설정을 할 수 있다.
    하지만 실제 db에 설정해서 사용하는 것이 여러가지 이유로 보편적인 사용이다.
 */
public class Users {
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
    private String name;
    private String email;
    // jpa에서 생성과 수정된 시간은 일반적으로 도메인에 항상 포함되게 되어있다.
    @Column(name = "created_at")
    private LocalDateTime createAt;
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Address> address;
}
