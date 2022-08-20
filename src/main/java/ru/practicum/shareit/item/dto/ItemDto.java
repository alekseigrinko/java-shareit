package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Некорректно имя объекта")
    private String name;
    @NotBlank(message = "Некорректное описание объекта")
    private String description;
    @NotNull(message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
}
