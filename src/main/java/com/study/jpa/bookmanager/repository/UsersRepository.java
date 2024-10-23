package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UsersRepository extends JpaRepository<Users, Long> {
    List<Users> findByName(String name);
    Users findUserByEmail(String email);
    Users findByEmail(String email);
    Users getByEmail(String email);
    Users readByEmail(String email);
    Users findFirstByName(String name);
    Users findTop1ByName(String name);
    Users findLastByName(String name);
    List<Users> findByEmailAndName(String email, String name);
    List<Users> findByEmailOrName(String email, String name);
    List<Users> findByCreateAtAfter(LocalDateTime yesterday);
    List<Users> findByIdAfter(Long id);
    List<Users> findByCreateAtGreaterThan(LocalDateTime yesterday);
    List<Users> findByCreateAtGreaterThanEqual(LocalDateTime yesterday);
    List<Users> findByCreateAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);
    List<Users> findByIdBetween(Long id1, Long id2);
    List<Users> findByIdIsNotNull();
    List<Users> findByAddressIsNotEmpty();
    List<Users> findByNameIn(List<String> names);
    List<Users> findByNameStartingWith(String name);
    List<Users> findByNameEndingWith(String name);
    List<Users> findByNameContains(String name);

    // is 메서드는 find user by name is name 이라고 생각하면 된다.
    Set<Users> findUserByNameIs(String name);
    Set<Users> findUserByName(String name);
    Set<Users> findUserByNameEquals(String name);

    List<Users> findLast1ByName(String name);
    List<Users> findTop1ByNameOrderByIdDesc(String name);
    List<Users> findTopByNameOrderByIdDesc(String name);
    List<Users> findFirstByNameOrderByIdDesc(String name);
    List<Users> findFirstByNameOrderByIdDescEmailAsc(String name);

    List<Users> findFirstByName(String name, Sort sort);

    Page<Users> findByName(String name, Pageable pageable); // Page는 응답 값, Pageable은 요청 값이다.

    @Query(value = "select * from users limit 1;", nativeQuery = true)
    Map<String, Object> findRawRecord();
}
