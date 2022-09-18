package ru.practicum.common.error_handling.exception;

public class IncorrectActionException extends RuntimeException {
    public IncorrectActionException(String message) {
        super(message);
    }
}
