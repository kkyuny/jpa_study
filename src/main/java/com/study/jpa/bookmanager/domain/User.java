package com.study.jpa.bookmanager.domain;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@NoArgsConstructor // jpa에서는 인자가 없는 생성자가 필요하기 때문에 선언한다.
@AllArgsConstructor
// @RequiredArgsConstructor // @NonNull이 선언된 필드에 대해 생성자를 생성
@Data // jpa에서 가장 많이 사용될 어노테이션이다.(ToString, Getter 등의 어노테이션을 한번에 생성)
@Builder // 빌더의 형식을 갖고 생성자를 생성
public class User {
    private String name;
    private String email;
    // jpa에서 생성과 수정된 시간은 일반적으로 도메인에 항상 포함되게 되어있다.
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
