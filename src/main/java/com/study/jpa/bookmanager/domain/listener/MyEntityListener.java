package com.study.jpa.bookmanager.domain.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class MyEntityListener {
    // 로직의 공통적인 부분은 @EntityListeners(value = MyEntityListener.class)를 통하여 공통적으로 처리할 수 있다.
    @PostPersist
    public void prePersist(Object o){
        if (o instanceof Auditable) {
            ((Auditable) o).setCreateAt(LocalDateTime.now());
        }
    }

    @PostUpdate
    public void preUpdate(Object o){
        if (o instanceof Auditable) {
            ((Auditable) o).setUpdateAt(LocalDateTime.now());
        }
    }
}
