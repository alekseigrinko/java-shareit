package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithComment {
    private Long id;
    @NotBlank(groups = {Create.class}, message = "Некорректно имя объекта")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Некорректное описание объекта")
    private String description;
    @NotNull(groups = {Create.class}, message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
    private List<CommentDtoForReturnItem> comments;
}
