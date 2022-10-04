package ru.practicum.server.user;

import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static BookingResponseDto.UserResponseDtoForBooker toUserDtoForReturnByBooker(User user) {
        return new BookingResponseDto.UserResponseDtoForBooker(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
