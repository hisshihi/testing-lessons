package org.example.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDaoSpy extends UserDao {

    private UserDao userDao;

    public UserDaoSpy(UserDao userDao) {
        this.userDao = userDao;
    }

    private Map<Long, Boolean> anwsers = new HashMap<>();

    @Override
    public boolean delete(Long userId) {
        return anwsers.getOrDefault(userId, userDao.delete(userId));
    }
}
