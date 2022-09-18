package ru.practicum.common.error_handling.exception;

public class StatisticServiceUnavailableException extends RuntimeException {
    public StatisticServiceUnavailableException(String message) {
        super(message);
    }
}
