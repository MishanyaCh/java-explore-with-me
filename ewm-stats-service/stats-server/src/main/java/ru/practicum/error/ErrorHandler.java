package ru.practicum.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.controller.StatisticController;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = {StatisticController.class})
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleIllegalArgumentException(final IllegalArgumentException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }
}
