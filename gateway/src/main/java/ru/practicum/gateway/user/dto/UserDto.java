package ru.practicum.gateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class}, message = "Некорректное имя пользователя")
    private String name;
    @Email(groups = {Create.class}, message = "Некорректный Email")
    @NotNull(groups = {Create.class})
    private String email;
}
