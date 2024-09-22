package com.study.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void test() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("test");

        System.out.println(">>> " + user);
    }
}