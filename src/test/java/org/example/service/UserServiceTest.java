package org.example.service;


import org.example.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private static final User ARINA = User.of(1L, "Arina", "123");
    private static final User REHAB = User.of(2L, "rehab", "111");
    private UserService userService;

    @BeforeAll
    void init() {
//        System.out.println("Before all");
    }

    @BeforeEach
    void prepare() {
//        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
//    Проверяем пустая ли коллекция пользователей
    void usersEmptyIfNoUserAdded() {
//        System.out.println("Test 1: " + this);

        List<User> users = userService.getAll();

        // Проверяет результат который возвращает true
        assertTrue(users.isEmpty(), () -> "User list is empty");
    }

    @Test
    void usersSizeIfUserAdded() {
//        System.out.println("Test 2: " + this);

        userService.add(ARINA);
        userService.add(REHAB);

        List<User> users = userService.getAll();

        // Проверяем, что кол-во пользователей равно 2
        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExists() {
        // Шаг 1 - подготовка данных
        userService.add(REHAB);

        // Шаг 2 - запрос на проверяемый функционал
        Optional<User> maybeUser = userService.login(REHAB.getUsername(), REHAB.getPassword());

        // Шаг 3 - проверка результата
        assertThat(maybeUser).isPresent();
        // Проверяем правльно ли найден пользователь
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(REHAB));
//        assertTrue(maybeUser.isPresent());
//        maybeUser.ifPresent(user -> assertEquals(REHAB, user));

    }

    @Test
    void usersConvertedToMapById() {
        userService.add(REHAB, ARINA);

        Map<Long, User> users = userService.getAllConvertedById();

        // Проверяем все ассерты
        assertAll(
                () -> assertThat(users).containsKeys(REHAB.getId(), ARINA.getId()),
                () -> assertThat(users).containsValues(REHAB, ARINA)
        );
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
//        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
//        System.out.println("After all");
    }

}
