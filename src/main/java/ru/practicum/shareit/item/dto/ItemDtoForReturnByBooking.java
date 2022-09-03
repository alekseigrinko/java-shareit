package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForReturnByBooking {
    private long id;
    private String name;
    private Boolean available;
    private UserForReturnByBooker owner;
}