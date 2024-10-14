package com.study.jpa.bookmanager.repository;

import com.study.jpa.bookmanager.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    List<Users> findByName(String name);
    Users findUserByEmail(String email);
    Users findByEmail(String email);
    Users getByEmail(String email);
    Users readByEmail(String email);
    Users findFirstByName(String name);
    Users findTop1ByName(String name);
    Users findLastByName(String name);
}
