package org.example.service;


import org.example.dto.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    @Test
//    Проверяем пустая ли коллекция пользователей
    void usersEmptyIfNoUserAdded() {

        UserService userService = new UserService();
        List<User> users = userService.getAll();

        // Проверяет результат который возвращает true
        assertTrue(users.isEmpty(), () -> "User list is empty");
    }

}
