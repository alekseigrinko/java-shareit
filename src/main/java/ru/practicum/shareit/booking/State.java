package ru.practicum.shareit.booking;

import ru.practicum.shareit.exeption.BadRequestException;

import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<State> checkState(String line) {
        Optional<State> state = Optional.empty();
        try {
            state = Optional.of(State.valueOf(line));
        } catch (IllegalArgumentException e) {
            new BadRequestException("Unknown state: " + line);
        }
        return state;
    }
}
