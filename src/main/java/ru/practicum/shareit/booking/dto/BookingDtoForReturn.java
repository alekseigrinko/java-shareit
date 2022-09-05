package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForReturn {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoForReturnByBooking item;
    private UserForReturnByBooker booker;
    private Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDtoForReturnByBooking {
        private long id;
        private String name;
        private Boolean available;
        private UserForReturnByBooker owner;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserForReturnByBooker {
        private Long id;
        private String name;
        private String email;
    }
}
