package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForReturn {

    private Long id;
    @NotBlank(groups = {Create.class}, message = "Некорректно имя объекта")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Некорректное описание объекта")
    private String description;
    @NotNull(groups = {Create.class}, message = "Статус не может быть пустым")
    private Boolean available;
    @NotNull(groups = {Create.class}, message = "Отсутствует пользователь-регистратор")
    private Long owner;
    private BookingDtoForReturn lastBooking;
    private BookingDtoForReturn nextBooking;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDtoForReturn {
        @NotNull
        private long id;
        @NotNull
        private long booker;
    }
}

