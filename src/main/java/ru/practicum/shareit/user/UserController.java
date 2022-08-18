package ru.practicum.shareit.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @PostMapping
    User createUser(@RequestHeader("X-Later-User-Id")
                       @Validated({Create.class}) @RequestBody User user) {
        return user;
    }

    @PutMapping
    User updateUser(@Validated ({Update.class})@RequestBody User user) {
        return user;
    }

    @DeleteMapping
    String deleteUser(@RequestBody User user) {
        return " ";
    }
}
