package org.example.service;


import org.example.TestBase;
import org.example.dao.UserDao;
import org.example.dto.User;
import org.example.extension.ConditionalExtension;
import org.example.extension.PostProcessingExtension;
import org.example.extension.ThrowableExtension;
import org.example.extension.UserServiceParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Для того, чтобы запускать тесты по порядку, нужно использовать
@TestMethodOrder(MethodOrderer.MethodName.class)
// Эта аннотация указывает JUnit использовать расширение UserServiceParamResolver, чтобы разрешать зависимости перед тестами.
@ExtendWith({
        UserServiceParamResolver.class,
        PostProcessingExtension.class,
        ConditionalExtension.class,
//        ThrowableExtension.class
})
class UserServiceTest extends TestBase {

    private static final User ARINA = User.of(1L, "Arina", "123");
    private static final User REHAB = User.of(2L, "rehab", "111");
    private UserService userService;
    private UserDao userDao;

    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of("Arina", "123", Optional.of(ARINA)),
                Arguments.of("rehab", "111", Optional.of(REHAB))
//                Arguments.of("rehab", "111", Optional.empty()),
//                Arguments.of("rehab", "111", Optional.empty())
        );
    }

    @BeforeAll
    void init() {
//        System.out.println("Before all");
    }

    @BeforeEach
    void prepare() {
//        System.out.println("Before each: " + this);
        // Использовал mock, чтобы не вызывать реальный объект
        this.userDao = Mockito.mock(UserDao.class);
        this.userService = new UserService(userDao);
    }

    @Test
    void shouldDeleteExistedUser() {
        userService.add(REHAB);
        // Это stub
        /*
        * Что делает эта строчка
        * Верни true когда userDao вызываем метод delete и передаём id
        * */
        // Более универсальный
        Mockito.doReturn(true).when(userDao).delete(REHAB.getId());

        // Подходит не для всех случаев
        // Есть возможность выполнять проверку последоватьно, для первого вызова будет true а для оставшихся будет false
//        Mockito.when(userDao.delete(REHAB.getId()))
//                .thenReturn(true)
//                .thenReturn(false);

        boolean deleteUser = userService.delete(REHAB.getId());
//        System.out.println(userService.delete(REHAB.getId()));
//        System.out.println(userService.delete(REHAB.getId()));

        assertThat(deleteUser).isTrue();
    }

    @Test
    @Order(1)
    @DisplayName("users will be empty if no user added")
//    Проверяем пустая ли коллекция пользователей
    void usersEmptyIfNoUserAdded(UserService userService) throws IOException {
        if (true) {
            throw new RuntimeException();
        }


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
    void usersConvertedToMapById() {
        userService.add(REHAB, ARINA);

        Map<Long, User> users = userService.getAllConvertedById();

        // Проверяем все ассерты
        assertAll(
                () -> assertThat(users).containsKeys(REHAB.getId(), ARINA.getId()),
                () -> assertThat(users).containsValues(REHAB, ARINA)
        );
    }

    @AfterEach
    void deleteDataFromDatabase() {
//        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
//        System.out.println("After all");
    }

    // Обозначается, как класс с тестами
    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class LoginTest {

        // Также можно указать время выполнения теста с помощью assertTimeout
        @Test
        void checkLoginFunctionalityPerformance() {
//            Optional<User> user = assertTimeout(Duration.ofMillis(200), () -> {
//                Thread.sleep(300);
//                return userService.login("rehab", REHAB.getPassword());
//            });

            // Также можно указать, что тест будет выполняться в отдельном потоке
            System.out.println(Thread.currentThread().getName());
            Optional<User> user = assertTimeoutPreemptively(Duration.ofMillis(400), () -> {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(300);
                return userService.login(REHAB.getUsername(), REHAB.getPassword());
            });

        }

        @Test
        // Помечаем тест как flaky и отключаем его выполнение
        @Disabled("flaky, need to see")
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
        void throwExceptionIfUsernameIsNull() {
            // Проверка сразу нескольких исключений
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "password")),
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("rehab", null))
            );
            //            Проверка исключений
            assertThrows(IllegalArgumentException.class, () -> userService.login(null, "password"));
        }

        //        @Test
        // Добавляем несклько выполнений тестов
        // Указываем сколько раз будет выполняться тест и как будет отображаться
        @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
        // Также можно указать в аргументах, что мы хотим вывести более подробную информацию с помощью RepetitionInfo
        void loginFailIfPasswordIsNotCorrect(RepetitionInfo repetitionInfo) {
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

        @ParameterizedTest(name = "{arguments} test")
        // Аннотация @ParameterizedTest используется для параметризованных тестов.
// Тест будет запущен несколько раз с разными входными параметрами.
// {arguments} в аннотации name позволяет выводить аргументы теста в отчёте о тестах для каждого прогноза.

//    @ArgumentsSource()
        // Подставляет null парамтер(но только 1 как и для всех нижних)
//        @NullSource
//        @EmptySource
//        @ValueSource(strings = {
//                "Denis", "Arina"
//        })
        @MethodSource("org.example.service.UserServiceTest#getArgumentsForLoginTest")
        // Аннотация @MethodSource указывает на метод, который возвращает набор аргументов для теста.
// В данном случае это метод getArgumentsForLoginTest() в классе UserServiceTest.

//        @CsvFileSource(resources = "/login-test-data.csv", delimiter = ',', numLinesToSkip = 1)
//        @CsvSource({
//                "Arina,123",
//                "rehab,111"
//        })
        @DisplayName("login param name")
        void loginParametrizetTest(String username, String password, Optional<User> user) {
            userService.add(REHAB, ARINA);
            Optional<User> maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);
        }

    }

}
