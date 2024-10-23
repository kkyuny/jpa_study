package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Gender;
import com.study.jpa.bookmanager.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class UsersRepositoryTest4 {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void insertAndUpdate() {
        Users user = new Users();
        user.setName("test111");
        user.setEmail("test111@test.com");

        usersRepository.save(user);
        /* insert 될 때 updatable = false로 설정했기 때문에 create_at의 값이 입력된다.
            하지만 일반적으로 create_at 값이 입력된다.
         */

        Users user2 = usersRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("test222");

        usersRepository.save(user2);
        /* id 1L의 user가 업데이트 된다.
            update 될 때 insertable = false로 설정했기 때문에 update_at의 값이 갱신된다.
         */
        System.out.println(user2);
    }

    @Test
    void enumTest() {
        Users user = new Users();
        user.setName("test111");
        user.setEmail("test111@test.com");
        usersRepository.save(user);

        usersRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);

        usersRepository.save(user);

        usersRepository.findAll().forEach(System.out::println);

        System.out.println(usersRepository.findRawRecord().get("gender"));
        /*
            @Enumerated(value = EnumType.STRING)의 설정이 없다면 결과값이 MALE이 저장 된 순서인 0이 나온다.
            이 때, MALE의 저장순서가 변경되면 잘못된 값이 매핑되는 문제가 발생할 수 있다.
            이를 위해 EnumType.STRING 설정을 하게 된다면 순서에 상관 없이 MALE이 조회된다.
         */
    }
}
