package ru.practicum.gateway.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.user.dto.Create;
import ru.practicum.gateway.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    ResponseEntity<Object> createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Registered user with name: {}", userDto.getName());
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                       @PathVariable long userId) {
        log.info("Update user with ID: {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping
    ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Get user with ID: {}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Delete user with ID: {}", userId);
        return userClient.deleteUser(userId);
    }
}
