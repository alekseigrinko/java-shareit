package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Create.class}, message = "Некорректно имя объекта")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Некорректное описание объекта")
    private String description;
    @NotNull(groups = {Create.class}, message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
}
