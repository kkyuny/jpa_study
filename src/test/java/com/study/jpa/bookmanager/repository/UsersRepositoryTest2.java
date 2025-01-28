package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Users;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void 다양한_select() {
        // where에 and 조건을 추가하여 검색한다.
        System.out.println("findByEmailAndEmail: " + usersRepository.findByEmailAndName("test8@google.com", "test8"));

        // where에 or 조건을 추가하여 검색한다.
        System.out.println("findByEmailOrEmail: " + usersRepository.findByEmailOrName("test6@google.com", "test7"));

        // after(>), before(<): 크고 작은 것에 대한 조건이다. ex) 시간(생성일, 수정일 등등)
        System.out.println("findByCreateAtAfter: " + usersRepository.findByCreateAtAfter(LocalDateTime.now().minusDays(1L)));

        // id가 7L보다 큰 id가 출력된다.
        System.out.println("findByIdAfter: " + usersRepository.findByIdAfter(7L));

        // greaterThan은 after에 대한 범용적인 표현이다.
        System.out.println("findByCreateAtGreaterThan: " + usersRepository.findByCreateAtGreaterThan(LocalDateTime.now().minusDays(1L)));

        // Equal은 where에 = 조건을 추가한다.
        System.out.println("findByCreateAtGreaterThanEqual: " + usersRepository.findByCreateAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));

        // between은 where에 특정 범위를 추가한다. 그리고 해당하는 양끝의 값을 포함한다(IdGraterThanEqualAndIdLessThanEqual과 동일).
        System.out.println("findByCreateAtBetween: " + usersRepository.findByCreateAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween: " + usersRepository.findByIdBetween(3L, 6L));

        // where에 is not null 조건을 추가하여 검색한다.
        System.out.println("findByIdIsNotNull: " + usersRepository.findByIdIsNotNull());
        // not empty는 컬렉션 타입의 empty를 체크한다.
        Users user = new Users();
        user.setName("new test");
        user.setEmail("test@test.com");
        user.setAddress(null);

        // where 조건에 exists를 이용하여 값을 찾는다.
        // name is not null and name != ''이 아니다.
        System.out.println("findByAddressIsNotEmpty: " + usersRepository.findByAddressIsNotEmpty());

        // where 조건에 in절을 이용하여 값을 찾는다.
        // 보통 파라미터에 List 형태의 쿼리 결과 값을 넣어서 사용한다.
        System.out.println("findByNameIn: " + usersRepository.findByNameIn(Lists.newArrayList("test3", "test2")));

        // like 검색이다. 근데 select 쿼리가 많이 실행되네
        System.out.println("findByNameStartingWith: " + usersRepository.findByNameStartingWith("te"));
        System.out.println("findByNameEndingWith: " + usersRepository.findByNameEndingWith("st3"));
        System.out.println("findByNameContains: " + usersRepository.findByNameContains("est"));
    }
}
