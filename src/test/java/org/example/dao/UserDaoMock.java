package org.example.dao;

import org.mockito.stubbing.Answer1;

import java.util.HashMap;
import java.util.Map;

public class UserDaoMock extends UserDao {

    // По id верни true, иначе false
    private Map<Long, Boolean> answers = new HashMap<>();
//    private Answer1<Long, Boolean> answer1;

    // Как работает mock
    /*
    * Он наследуется от суперкласса(в данном случае UserDao)
    * и наследует все его методы.
    * Которые возращают стандартные значение ( к примеру для boolean это false)
    * */
    @Override
    public boolean delete(Long userId) {
        return answers.getOrDefault(userId, false);
    }
}
