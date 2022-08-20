package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Некорректное имя пользователя")
    private String name;
    @Email(message = "Некорректный Email")
    @NotBlank(message = "Необходимо заполнить Email")
    private String email;
}
