package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * // TODO .
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank(groups = {Create.class})
    private String email;
}
