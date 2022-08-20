package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserDao {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUser(long userId);

    String deleteUser(Long userId);

    void checkUser(Long userId);
}
