package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dao.UserDaoImp;
import ru.practicum.shareit.user.model.User;

class UserDaoImpTest {

    private static UserDaoImp userDaoImp = new UserDaoImp();
    private static User user;

    @BeforeAll
    private static void setup() {
        user = new User(
          null, "name", "user@user.com"
        );
        userDaoImp.addUser(user);
    }

    @Test
    void addUser() {
        Assertions.assertEquals(userDaoImp.getAllUsers().size(), 1);
    }

    @Test
    void updateUser() {
        User user2 = new User(
                null, "name2", null
        );
        userDaoImp.updateUser(1L,user2);
        Assertions.assertEquals(userDaoImp.getUser(1).getName(), "name2");
    }

    @Test
    void deleteUser() {
        userDaoImp.deleteUser(1L);
        Assertions.assertEquals(userDaoImp.getAllUsers().size(), 0);
    }
}