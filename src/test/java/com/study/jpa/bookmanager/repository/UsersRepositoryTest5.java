package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Gender;
import com.study.jpa.bookmanager.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UsersRepositoryTest5 {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    void listenerTest(){
        Users user = new Users();
        user.setName("test");
        user.setEmail("test@test.com");

        usersRepository.save(user);
        Users user2 = usersRepository.findById(1L).orElseThrow(RuntimeException::new);

        user2.setName("test2");

        usersRepository.save(user2);

        usersRepository.deleteById(4L);
    }

    @Test
    void prePersistTest(){
        Users user = new Users();
        user.setName("test");
        user.setEmail("test@test.com");

        // 날짜 세팅 같은 경우 실수로 놓칠 수 있는 경우가 생기기 때문에 prePersist에서 수행하는 것도 좋다.
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        usersRepository.save(user);

        System.out.println(usersRepository.findByEmail("test@test.com"));

        Users user2 = new Users();
        user2.setName("test2");
        user2.setEmail("test2@test.com");

        usersRepository.save(user2);

        // 날짜 세팅을 하지 않았지만 @PrePersist에 의해 자동으로 현재시간이 입력된다.
        System.out.println(usersRepository.findByEmail("test2@test.com"));
    }

    @Test
    void preUpdateTest(){

        Users user = usersRepository.findById(3L).orElseThrow(RuntimeException::new);

        System.out.println("as-is: " + user);

        user.setName("test333");
        usersRepository.save(user);

        System.out.println("to-be: " + usersRepository.findAll().get(0));
    }

    @Test
    void userHistoryTest(){
        Users user = new Users();

        user.setName("test");
        user.setEmail("test@test.com");

        usersRepository.save(user);

        user.setName("test2");
        usersRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }
}
