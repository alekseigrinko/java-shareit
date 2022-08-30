package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Service("UserServiceByRepository")
@Slf4j
public class UserServiceByRepository implements UserService {

    final private UserRepository userRepository;

    public UserServiceByRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        checkUser(userId);
        User userInMemory = userRepository.findById(userId).get();
        if (userDto.getName() != null) {
            userInMemory.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userInMemory.setEmail(userDto.getEmail());
        }
        log.debug("Данные пользователя обновлены: " + userInMemory);
        return toUserDto(userRepository.save(userInMemory));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long userId) {
        checkUser(userId);
        return toUserDto(userRepository.findById(userId).get());
    }

    @Override
    public String deleteUser(long userId) {
        userRepository.deleteById(userId);
        return "Пользователь ID" + userId + " удален";
    }

    public void checkUser(long userId){
        if(!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }
}
