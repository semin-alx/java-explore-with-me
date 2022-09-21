package ru.practicum.common.error.exception;

public class IncorrectActionException extends RuntimeException {
    public IncorrectActionException(String message) {
        super(message);
    }
}
