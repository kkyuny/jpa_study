package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Users;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UsersRepositoryTest {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void crud() {
        // findAll: 테이블의 모든 값을 가져온다. 실무에서는 성능상의 이슈(ex. 1000만건 이상의 데이터가 조회 되는 등)로 사용하지 않는다.
        // findAll(Sort sort): sort된 값으로 가져올 수 있다.
        // findAllById(Iterable<ID> ids): id 값을 list 형태로 받아 in 구문으로 레코드를 조회한다.
        // 그 외 CrudRepository에서 더 많은 메서드를 확인할 수 있다.
        /*
            <S extends T> S save(S entity);
            <S extends T> Iterable<S> saveAll(Iterable<S> entities);
            Optional<T> findById(ID id);
            boolean existsById(ID id);
            Iterable<T> findAll();
            Iterable<T> findAllById(Iterable<ID> ids);
            long count();
            void deleteById(ID id);
            void delete(T entity);
            void deleteAllById(Iterable<? extends ID> ids);
            void deleteAll(Iterable<? extends T> entities);
            void deleteAll();
         */
        // 쿼리문에 오더절이 추가된다.
        //List<Users> users = usersRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

        // 쿼리문에 where u1_0.id in (?, ?, ?)이 추가 된다.
        //List<Users> users = usersRepository.findAllById(Lists.newArrayList(1L, 2L, 3L));

        //users.forEach(System.out::println);

        Users user1 = new Users("jack", "jack@test.com");
        Users user2 = new Users("steve", "steve@test.com");

        usersRepository.saveAll(Lists.newArrayList(user1, user2));

        usersRepository.findAll().forEach(System.out::println);

        Optional<Users> user3 = usersRepository.findById(3L); // findById는 Optional 속성으로 값을 가져온다.
        Users user4 = usersRepository.getOne(4L);
    }
}