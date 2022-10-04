package ru.practicum.server.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.server.user.UserMapper.toUser;

class UserServiceImpTest {

    UserService userService;
    UserRepository userRepository;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImp(userRepository);
        userDto = new UserDto(1L, "user", "user@user.com");
    }

    @Test
    void addUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(toUser(userDto));

        UserDto userDtoTest = userService.addUser(userDto);

        assertEquals("user@user.com", userDtoTest.getEmail());
    }

    @Test
    void updateUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(toUser(userDto)));
        UserDto userDtoUpdate = new UserDto(null, "userUpdate", null);
        userDto.setName(userDtoUpdate.getName());
        when(userRepository.save(any(User.class)))
                .thenReturn(toUser(userDto));

        UserDto userDtoTest = userService.updateUser(1L, userDtoUpdate);

        assertEquals("userUpdate", userDtoTest.getName());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(toUser(userDto)));
        List<UserDto> userList = userService.getAllUsers();
        assertEquals(1, userList.size());
        assertEquals("user@user.com", userList.get(0).getEmail());
    }

    @Test
    void getUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(toUser(userDto)));

        UserDto userDtoTest = userService.getUser(1L);

        assertEquals("user", userDtoTest.getName());
    }

    @Test
    void checkUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            UserDto userDtoTest = userService.updateUser(5L, userDto);
        });

        assertEquals("Пользователь ID: " + 5 + ", не найден!", thrown.getMessage());
    }
}