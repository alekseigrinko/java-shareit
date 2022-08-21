package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Service
public class UserServiceImp implements UserService {

    UserDao userDao;

    public UserServiceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return toUserDto(userDao.addUser(toUser(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        return toUserDto(userDao.updateUser(userId, toUser(userDto)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long userId) {
        return toUserDto(userDao.getUser(userId));
    }

    @Override
    public String deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
