package ru.practicum.server.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    private String description;
    private long requester;
    private LocalDateTime created;
}
