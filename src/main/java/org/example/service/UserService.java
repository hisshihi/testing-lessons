package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.User;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class UserService {

    // Список пользователей, который будет хранить объекты User
    private final List<User> users = new ArrayList<>();
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean delete(Long userId) {
        if (userId != null) {
            Long userLocalId = 25L;
            return userDao.delete(userId);
        } else return false;
    }

    // Метод для получения всех пользователей
    public List<User> getAll() {
        return users;
    }

    public void add(User... users) {
        // Добавляет переданных пользователей в список
        this.users.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String username, String password) {

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

    // Метод для получения всех пользователей в виде карты, где ключ - это ID пользователя
    public Map<Long, User> getAllConvertedById() {
        return users.stream() // Превращает список пользователей в поток
                .collect(toMap(User::getId, identity())); // Собирает в карту
    }
}
