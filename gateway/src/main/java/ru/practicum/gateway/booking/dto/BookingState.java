package ru.practicum.gateway.booking.dto;

import ru.practicum.gateway.exeption.BadRequestException;

import java.util.Optional;

public enum BookingState {
	// Все
	ALL,
	// Текущие
	CURRENT,
	// Будущие
	FUTURE,
	// Завершенные
	PAST,
	// Отклоненные
	REJECTED,
	// Ожидающие подтверждения
	WAITING;

	public static Optional<BookingState> from(String stringState) {
		boolean check = false;
		for (BookingState state: BookingState.values()) {
			if (state.toString().equals(stringState)) {
				check = true;
				break;
			}
		}
		if (!check) {
			throw new BadRequestException("Unknown state: " + stringState);
		}
		return Optional.of(BookingState.valueOf(stringState));
	}
}
