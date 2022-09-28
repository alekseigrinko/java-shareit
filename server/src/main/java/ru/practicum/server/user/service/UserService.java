package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUser(long userId);

    String deleteUser(long userId);
}
