package com.study.jpa.bookmanager.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class UsersRepositoryTest3 {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void pagingAndSortingTest() {
        System.out.println("findTop1ByName: " + usersRepository.findTop1ByName("test3")); // 가장 첫번째 test3이 조회된다.
        System.out.println("findLast1ByName: " + usersRepository.findLast1ByName("test3")); // Last1은 의도대로 동작되지 않고 모든 test3을 가져온다.
        System.out.println("findTop1ByNameOrderByIdDesc: " + usersRepository.findTop1ByNameOrderByIdDesc("test3")); // last는 order by desc 후 가져온다.
        System.out.println("findTopByNameOrderByIdDesc: " + usersRepository.findTopByNameOrderByIdDesc("test3")); // last는 order by desc 후 가져온다.
        System.out.println("findFirstByNameOrderByIdDesc: " + usersRepository.findFirstByNameOrderByIdDesc("test3")); // last는 order by desc 후 가져온다.
        System.out.println("findFirstByNameOrderByIdDescEmailAsc: " + usersRepository.findFirstByNameOrderByIdDescEmailAsc("test3")); // 추가 정렬도 가능하다. (order by u1_0.id desc, u1_0.email)
        // 메서드에 정렬에 필요한 이름을 추가하면 코드 가독성이 떨어질 수 있기 때문에 기본 메서드에 Sort class를 사용하여 코드가독성을 높일 수 있다.
        System.out.println("findFirstByNameWithSortParams: " + usersRepository.findFirstByName("test3", Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email")))); // Sort를 이용하여 정렬할 수도 있다.
        System.out.println("findFirstByNameWithSortParams: " + usersRepository.findFirstByName("test3", getSort())); // sort를 메서드화하여 사용할 수도 있다.
        // 상황에 따라서 네이밍 컨벤션? 기반의 sort를 사용할 것인지 파라미터 기반의 sort를 사용할 것인지 잘 선택해야한다.

        // 첫 번째 페이지의 size 만큼의 값을 가져온다.
        System.out.println("findByNameWithPaging: " + usersRepository.findByName("test3", PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")))).getContent());
        // 두 번째 페이지의 size 만큼의 값을 가져온다.
        System.out.println("findByNameWithPaging: " + usersRepository.findByName("test3", PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")))).getContent());
        // total count
        System.out.println("findByNameWithPaging: " + usersRepository.findByName("test3", PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")))).getTotalElements());
    }

    private Sort getSort(){
        return Sort.by(
                Sort.Order.desc("id"),
                Sort.Order.asc("email")
        );
    }
}
