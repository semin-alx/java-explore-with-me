package ru.practicum.common.error_handling;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ApiError {

    private static final String TIMESTAMP_FORMAT = "dd.MM.yyyy hh:mm:ss";

    private String id;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
    private String[] errors;

    private String[] getStackTrace(Exception e) {

        List<String> list = new ArrayList<>();

        for (StackTraceElement el : e.getStackTrace()) {
            list.add(el.toString());
        }

        return list.toArray(new String[list.size()]);

    }

    private String getStringTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
        return formatter.format(LocalDateTime.now());
    }

    public ApiError(Exception e, HttpStatus httpStatus, String message) {

        this.message = message;
        this.reason = e.getMessage();
        this.errors = getStackTrace(e);
        this.id = UUID.randomUUID().toString();
        this.timestamp = getStringTimestamp();
        this.status = httpStatus.name();

    }

}
