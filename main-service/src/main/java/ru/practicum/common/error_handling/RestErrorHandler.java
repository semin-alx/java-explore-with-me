package ru.practicum.common.error_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.error_handling.exception.IncorrectActionException;
import ru.practicum.common.error_handling.exception.ObjectNotFoundException;
import ru.practicum.common.error_handling.exception.StatisticServiceUnavailableException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@RestControllerAdvice
public class RestErrorHandler {

    private ResponseEntity<ApiError> createResponse(Exception e, HttpStatus httpStatus,
                                                    String message) {
        ApiError apiError = new ApiError(e, httpStatus, message);
        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> constraintViolationExceptionHandler(final ConstraintViolationException e) {
        return createResponse(e, HttpStatus.BAD_REQUEST,
                "Запрос составлен неверно");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return createResponse(e, HttpStatus.BAD_REQUEST,
                "Неверные параметры в операции");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException e) {
        return createResponse(e, HttpStatus.BAD_REQUEST,
                "Запрос составлен неверно");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> sqlExceptionHandler(final SQLException e) {
        return createResponse(e, HttpStatus.FORBIDDEN,
                "Ошибка чтения/записи в базу данных");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> incorrectActionExceptionHandler(final IncorrectActionException e) {
        return createResponse(e, HttpStatus.FORBIDDEN,
                "Недопустимое действие с объектом");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> objectNotFoundExceptionHandler(final ObjectNotFoundException e) {
        return createResponse(e, HttpStatus.NOT_FOUND,
                "Запрашиваемый объект в базе данных не найден");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> statisticServiceUnavailableExceptionHandler(
            final StatisticServiceUnavailableException e) {
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR,
                "Сервис статистики не работает");
    }

}
