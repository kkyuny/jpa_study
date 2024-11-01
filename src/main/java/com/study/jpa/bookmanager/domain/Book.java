package com.study.jpa.bookmanager.domain;


import com.study.jpa.bookmanager.domain.listener.Auditable;
import com.study.jpa.bookmanager.domain.listener.MyEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@EntityListeners(value = MyEntityListener.class)
public class Book implements Auditable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String author;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    /*@PrePersist
    public void prePersist(){
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    public void perUpdate(){
        this.updateAt = LocalDateTime.now();
    }*/
}
