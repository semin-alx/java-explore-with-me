package ru.practicum.common.error.exception;

public class StatisticServiceUnavailableException extends RuntimeException {
    public StatisticServiceUnavailableException(String message) {
        super(message);
    }
}
