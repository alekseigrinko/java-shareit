package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("UserDaoImp")
@Slf4j
public class UserDaoImp implements UserDao {

    private HashMap<Long, User> userMap = new HashMap<>();
    private Long id = 0L;

    private Long createId() {
        id++;
        return id;
    }

    private UserMapper userMapper = new UserMapper();

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
       /* if (userDto.getEmail() == null) {
            log.debug("Email не может быть пустой");
            throw new ValidationException("Email не может быть пустой");
        }*/
        checkUserEmail(user);
        user.setId(createId());
        userMap.put(user.getId(), user);
        log.debug("Зарегистрирован пользователь: " + user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        checkUser(userId);
        User user = userMap.get(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkUserEmail(userMapper.toUser(userDto));
            user.setEmail(userDto.getEmail());
        }
        user.setId(userId);
        userMap.remove(userId);
        userMap.put(user.getId(), user);
        log.debug("Данные пользователя обновлены: " + user);
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsersList = new ArrayList<>();
        for (User user: userMap.values()) {
            allUsersList.add(userMapper.toUserDto(user));
        }
        log.debug("Получен список всех пользователей");
        return allUsersList;
    }

    @Override
    public UserDto getUser(long userId) {
        checkUser(userId);
        log.debug("Предоставлены данные пользователя ID: " + userId);
        return userMapper.toUserDto(userMap.get(userId));
    }

    @Override
    public String deleteUser(Long userId) {
        checkUser(userId);
        userMap.remove(userId);
        log.debug("Удален пользователь ID: " + userId);
        return "Удален пользователь ID: " + userId;
    }

    @Override
    public void checkUser(Long userId) {
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
