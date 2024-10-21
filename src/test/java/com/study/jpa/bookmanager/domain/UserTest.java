package com.study.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void test() {
        Users user = new Users();
        user.setEmail("test@test.com");
        user.setName("test");

        System.out.println(">>> " + user);
    }
}