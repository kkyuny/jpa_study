package com.study.jpa.bookmanager.domain;

import com.study.jpa.bookmanager.domain.listener.Auditable;
import com.study.jpa.bookmanager.domain.listener.MyEntityListener;
import com.study.jpa.bookmanager.domain.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Data
@EntityListeners(value = {MyEntityListener.class})
public class UserHistory implements Auditable {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String name;
    private String email;
    @CreatedDate // AuditingEntityListener가 감지 시 CreatedDate를 입력한다.
    private LocalDateTime createAt;
    @LastModifiedDate // AuditingEntityListener가 감지 시 LastModifiedDate를 입력한다.
    private LocalDateTime updateAt;
}
