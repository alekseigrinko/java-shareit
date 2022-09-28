package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemResponseDtoForBooking item;
    private UserResponseDtoForBooker booker;
    private Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponseDtoForBooking {
        private long id;
        private String name;
        private Boolean available;
        private UserResponseDtoForBooker owner;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponseDtoForBooker {
        private Long id;
        private String name;
        private String email;
    }
}
