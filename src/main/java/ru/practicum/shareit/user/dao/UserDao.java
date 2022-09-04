package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User updateUser(long userId, User user);

    List<User> getAllUsers();

    User getUser(long userId);

    String deleteUser(long userId);

    void checkUser(long userId);
}
