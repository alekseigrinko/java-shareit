package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserService userService;

    public UserController(@Qualifier("UserServiceImp") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserDto createUser(@Valid
                                                     @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@Validated({Create.class})
                                                      @PathVariable long userId,
                                                      @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable long userId){
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable long userId) {
        return userService.deleteUser(userId);
    }
}