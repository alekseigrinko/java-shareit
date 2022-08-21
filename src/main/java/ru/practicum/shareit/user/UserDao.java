package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User updateUser(Long userId, User user);

    List<User> getAllUsers();

    User getUser(long userId);

    String deleteUser(Long userId);

    void checkUser(Long userId);
}
