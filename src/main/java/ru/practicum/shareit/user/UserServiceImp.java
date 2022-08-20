package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service("UserServiceImp")
public class UserServiceImp implements UserService {

    UserDao userDao;

    public UserServiceImp(@Qualifier("UserDaoImp") UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userDao.addUser(userDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        return userDao.updateUser(userId, userDto);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public UserDto getUser(long userId) {
        return userDao.getUser(userId);
    }

    @Override
    public String deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
