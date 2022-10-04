package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    @NotBlank(message = "Пустой комментарий")
    private String text;
    String authorName;
    LocalDateTime created;
}
