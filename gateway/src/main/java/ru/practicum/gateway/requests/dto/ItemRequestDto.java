package ru.practicum.gateway.requests.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotBlank
    private String description;
    private long requester;
    private LocalDateTime created;
}
