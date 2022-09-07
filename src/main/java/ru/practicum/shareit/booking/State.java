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
        boolean check = false;
        for (State state: State.values()) {
            if (state.toString().equals(line)) {
                check = true;
                break;
            }
        }
        if (!check) {
            throw new BadRequestException("Unknown state: " + line);
        }
        return Optional.of(State.valueOf(line));
    }
}
