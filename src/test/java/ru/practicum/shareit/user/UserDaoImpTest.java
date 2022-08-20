package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

class UserDaoImpTest {

    private static UserDaoImp userDaoImp = new UserDaoImp();
    private static UserDto userDto;

    @BeforeAll
    private static void setup() {
        userDto = new UserDto(
          null, "name", "user@user.com"
        );
        userDaoImp.addUser(userDto);
    }

    @Test
    void addUser() {
        Assertions.assertEquals(userDaoImp.getAllUsers().size(), 1);
    }

    @Test
    void updateUser() {
        UserDto userDto2 = new UserDto(
                null, "name2", null
        );
        userDaoImp.updateUser(1L,userDto2);
        Assertions.assertEquals(userDaoImp.getUser(1).getName(), "name2");
    }

    @Test
    void deleteUser() {
        userDaoImp.deleteUser(1L);
        Assertions.assertEquals(userDaoImp.getAllUsers().size(), 0);
    }
}