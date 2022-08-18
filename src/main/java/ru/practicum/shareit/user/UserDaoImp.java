package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;

@Repository
@Slf4j
public class UserDaoImp implements UserDao {
    private HashMap<Long, User> userMap = new HashMap<>();
    private Long id = 0L;

    private Long createId() {
        id++;
        return id;
    }
}
