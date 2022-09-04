package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class UserDaoImp implements UserDao {

    private HashMap<Long, User> userMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        checkUserEmail(user);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.debug("Зарегистрирован пользователь: " + user);
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        checkUser(userId);
        User userInMemory = userMap.get(userId);
        if (user.getName() != null) {
            userInMemory.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkUserEmail(user);
            userInMemory.setEmail(user.getEmail());
        }
        userInMemory.setId(userId);
        userMap.remove(userId);
        userMap.put(userInMemory.getId(), userInMemory);
        log.debug("Данные пользователя обновлены: " + user);
        return userInMemory;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsersList = new ArrayList<>();
        allUsersList.addAll(userMap.values());
        log.debug("Получен список всех пользователей");
        return allUsersList;
    }

    @Override
    public User getUser(long userId) {
        checkUser(userId);
        log.debug("Предоставлены данные пользователя ID: " + userId);
        return userMap.get(userId);
    }

    @Override
    public String deleteUser(long userId) {
        checkUser(userId);
        userMap.remove(userId);
        log.debug("Удален пользователь ID: " + userId);
        return "Удален пользователь ID: " + userId;
    }

    @Override
    public void checkUser(long userId) {
        if (!userMap.containsKey(userId)) {
            log.warn("Пользователя с ID " + userId + " не найдено!");
            throw new ObjectNotFoundException("Пользователя с ID " + userId + " не найдено!");
        }
    }

    private void checkUserEmail(User testUser) {
        for (User user : userMap.values()) {
            if (user.getEmail().equals(testUser.getEmail())) {
                log.warn("Email " + testUser.getEmail() + " уже используется!");
                throw new ValidationException("Email " + testUser.getEmail() + " уже используется!");
            }
        }
    }
}
