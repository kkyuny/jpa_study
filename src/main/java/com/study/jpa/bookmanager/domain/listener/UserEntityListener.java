package com.study.jpa.bookmanager.domain.listener;

import com.study.jpa.bookmanager.domain.UserHistory;
import com.study.jpa.bookmanager.domain.Users;
import com.study.jpa.bookmanager.repository.UserHistoryRepository;
import com.study.jpa.bookmanager.support.BeanUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

// @Component // 엔티티 리스너는 스프링 빈을 주입받지 못한다.
public class UserEntityListener {
    @PreUpdate
    @PrePersist
    public void prePersistAndPreUpdate(Object o){
        // 스프링 빈을 주입받지 못하기 때문에 BeanUtils를 생성하여 bean을 가져온다.
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        Users user = (Users) o;

        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        userHistoryRepository.save(userHistory);
    }
}
