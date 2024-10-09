package com.study.jpa.bookmanager.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersRepositoryTest2 {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void select() {
        // where 조건에 name이 추가되어 select를 하게 된다.
        // usersRepository에서 findByName을 선언한 데이터 타입으로 결과를 return하게 된다.
        // By와 컬럼명은 반드시 정확히 작성해야한다.
        System.out.println(usersRepository.findByName("test3"));

        // 다양한 name 형태로 select 쿼리를 사용할 수 있다.
        // naming에 오류가 있을 시 런타임 에러가 발생하기 때문에 test 코드에서 메서드들을 실행해보는 것이 좋다.
        System.out.println("findByEmail: " + usersRepository.findByEmail("test3@fastcampus.com"));
        System.out.println("findUserByEmail: " + usersRepository.findUserByEmail("test3@fastcampus.com"));
        System.out.println("getByEmail: " + usersRepository.getByEmail("test3@fastcampus.com"));
        System.out.println("readByEmail: " + usersRepository.readByEmail("test3@fastcampus.com"));

        // 강사님의 쿼리에서는 limit을 이용하여 조회가 되었지만 내 쿼리에서는 fetch를 이용하여 쿼리가 조회되었다.
        // 2번째 존재하는 결과가 필요하면 First2, Top2로 조회할 수 있다.
        // last 등 다른 조건이 필요할 땐 order by절을 사용해야한다.
        System.out.println("findFirstByName: " + usersRepository.findFirstByName("test8"));
        System.out.println("findTop1ByName: " + usersRepository.findTop1ByName("test8"));
    }
}
