package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Users;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;


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

        Users user1 = new Users();
        Users user2 = new Users();

        usersRepository.saveAll(Lists.newArrayList(user1, user2));

        usersRepository.findAll().forEach(System.out::println);

        Optional<Users> user3 = usersRepository.findById(3L); // findById는 Optional 속성으로 값을 가져온다.
        Users user4 = usersRepository.getOne(4L);
    }

    @Test
    void crud2() {
        Users user = new Users();
        user.setName("new test");
        user.setEmail("test@test.com");

        // flush는 db 반영 시점에 대한 기능을 제공한다. 추후에 더 학습할 예정이라고 하심.
        usersRepository.saveAndFlush(user);

        // 저장된 수를 count
        long count = usersRepository.count();
        System.out.println(count);

        // exists 메서드는 count를 호출하여 값을 처리한다.
        boolean exists = usersRepository.existsById(1L);
        System.out.println(exists);

        // select문이 2번 실행된다. delete문은 select로 먼저 id가 존재하는지 확인 후 삭제를 진행한다.
        usersRepository.delete(usersRepository.findById(1L).orElseThrow(RuntimeException::new));
        // select문이 1번 실행된다.
        usersRepository.deleteById(3L);
        usersRepository.findAll().forEach(System.out::println);
        // Iterable한 값을 받아 해당하는 id를 해당되는 횟수만큼 delete를 호출하여 삭제한다.
        usersRepository.deleteAll(usersRepository.findAllById(Lists.newArrayList(4L, 5L)));
        usersRepository.findAll().forEach(System.out::println);

        // delete 쿼리의 where에서 in절을 사용하여 delete문을 1번만 호출하여 삭제한다.
        usersRepository.deleteAllInBatch(usersRepository.findAllById(Lists.newArrayList(6L, 7L, 8L)));
    }

    @Test
    void crud3(){
        // 페이징 처리를 제공하는 프레임워크의 기능이다.
        // 전체 조회 목록 중 해당하는 페이지 넘버를 사이즈에 맞추어 값들을 처리한다.
        // pageNumber를 입력받아 페이징 처리를 할 수 있을 것 같다.
        Page<Users> users = usersRepository.findAll(PageRequest.of(0, 3));

        System.out.println("page: " + users);
        // count 쿼리와 동일하다.
        System.out.println("totalElements: " + users.getTotalElements());
        System.out.println("totalPages: " + users.getTotalPages());
        System.out.println("numberOfElements: " + users.getNumberOfElements());
        System.out.println("sort: " + users.getSort());
        System.out.println("size: " + users.getSize());

        users.getContent().forEach(System.out::println);
    }

    @Test
    void crud4(){
        // query by execute
        /* example은 jpa을 상속하고 있으며, matcher를 추가하여 원하는 쿼리를 만들 수 있는 기능을 제공한다.
            하지만 example이 생각만큼 쓰이지는 않는다고 한다.
            matcher에 대해 문자나 문자열만 쓸 수 있는 제약사항이 있기 때문이라고 한다.
            그래서 복잡한 쿼리가 필요할 때 쿼리 dsl?과 같은 별도의 방식을 사용한다고 한다.
         */
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", endsWith());

        Users users = new Users();

        users.setEmail("google.com");
        users.setName("test6");

        // name은 검사하지 않고 email만 endsWith로 like 검색을 한다.
        Example<Users> example = Example.of(users, matcher);
        usersRepository.findAll(example).forEach(System.out::println);

        // matcher 등의 옵션을 넣지 않는다면 exact 검색을 한다.(입력 값과 동일한 값만 조회)
        Example<Users> example2 = Example.of(users);
        usersRepository.findAll(example2).forEach(System.out::println);

        // contains는 양방향 like 옵션이다.
        ExampleMatcher matcher2 = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", contains());
    }

    @Test
    void crud5(){
        /* save 메서드는 기본적으로 @Transactional 어노테이션이 있다.
            save 메서드는 entity를 조회해서 isNew(null)이면 insert 아니면 update를 수행한다.
            saveAll은 findAll로 select를 실행 후 for문을 돌면서 save를 실행한다. 즉 여러번의 insert가 발생하게 된다.
            deleteAll도 findAll로 select를 실행 후 for문을 돌면서 delete를 실행한다.
            하지만 deleteAllInBatch는 전체를 where 조건 없이 혹은 in절을 이용하여 delete를 실행하기 때문에 성능 상 이점이 있다. */
        Users user = new Users();
        user.setName("test");
        user.setEmail("test@test.com");

        // insert문이 실행된다.
        usersRepository.save(user);

        user.setEmail("updateTest@test.com");
        // select문을 실행 후 update를 진행한다.
        usersRepository.save(user);
    }
}