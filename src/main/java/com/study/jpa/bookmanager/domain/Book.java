package com.study.jpa.bookmanager.domain;


import com.study.jpa.bookmanager.domain.listener.Auditable;
import com.study.jpa.bookmanager.domain.listener.MyEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EntityListeners(value = MyEntityListener.class)
public class Book extends BaseEntity implements Auditable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String category;
    private Long authorId;
    private Long publisherId;
}
