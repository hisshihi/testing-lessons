package org.example.service;


import org.example.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private static final User ARINA = User.of(1L, "Arina", "123");
    private static final User REHAB = User.of(2L, "rehab", "111");
    private UserService userService;

    @BeforeAll
    void init() {
        System.out.println("Before all");
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
//    Проверяем пустая ли коллекция пользователей
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test 1: " + this);

        List<User> users = userService.getAll();

        // Проверяет результат который возвращает true
        assertTrue(users.isEmpty(), () -> "User list is empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test 2: " + this);

        userService.add(ARINA);
        userService.add(REHAB);

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExists() {
        // Шаг 1 - подготовка данных
        userService.add(REHAB);

        // Шаг 2 - запрос на проверяемый функционал
        Optional<User> maybeUser = userService.login(REHAB.getUsername(), REHAB.getPassword());

        // Шаг 3 - проверка результата
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(REHAB, user));

    }

    @Test
    void loginFailIfPasswordIsNotCorrect() {
        userService.add(REHAB);
        Optional<User> user = userService.login(REHAB.getUsername(), "123");

        assertTrue(user.isEmpty());
    }

    @Test
    void loginFailIfUserDoesNotExist() {
        userService.add(REHAB);

        Optional<User> user = userService.login("Sergey", "123");

        assertTrue(user.isEmpty());
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all");
    }

}
