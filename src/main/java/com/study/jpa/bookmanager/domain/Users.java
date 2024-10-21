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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase
    private Long id;
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
