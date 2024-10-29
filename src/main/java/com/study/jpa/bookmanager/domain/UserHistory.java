package com.study.jpa.bookmanager.domain;

import com.study.jpa.bookmanager.domain.listener.Auditable;
import com.study.jpa.bookmanager.domain.listener.UserEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@EntityListeners(value = {AuditingEntityListener.class, UserEntityListener.class})
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
