package ru.practicum.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.controller.CategoryController;
import ru.practicum.compilation.controller.CompilationController;
import ru.practicum.event.controller.EventAdminController;
import ru.practicum.event.controller.EventPrivateController;
import ru.practicum.event.controller.EventPublicController;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.EventAlreadyCanceledException;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.exception.EventCreatorException;
import ru.practicum.exception.EventDateTimeException;
import ru.practicum.exception.EventNotPublishedException;
import ru.practicum.exception.NotEventCreatorException;
import ru.practicum.exception.NotParticipationRequestCreatorException;
import ru.practicum.exception.ObjectAlreadyExistException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ReachLimitException;
import ru.practicum.exception.UnknownStateActionException;
import ru.practicum.participationRequest.controller.ParticipationRequestController;
import ru.practicum.user.controller.UserController;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = {CategoryController.class, UserController.class,
        EventPrivateController.class, EventAdminController.class, EventPublicController.class,
        ParticipationRequestController.class, CompilationController.class})
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Error handleObjectNotFoundException(final ObjectNotFoundException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.NOT_FOUND.getReasonPhrase();
        String reason = "Не удалось найти требуемый объект";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleConstraintViolationException(final ConstraintViolationException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleEventDateTimeException(final EventDateTimeException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleNotEventCreatorException(final NotEventCreatorException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleNotParticipationRequestCreatorException(final NotParticipationRequestCreatorException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleUnknownStateActionException(final UnknownStateActionException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleIllegalArgumentException(final IllegalArgumentException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String reason = "Получен запрос с некорретными входными данными";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleObjectAlreadyExistException(final ObjectAlreadyExistException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Найдено дублирование объектов";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleEventAlreadyPublishedException(final EventAlreadyPublishedException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleEventAlreadyCanceledException(final EventAlreadyCanceledException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleEventNotPublishedException(final EventNotPublishedException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleEventCreatorExceptionException(final EventCreatorException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleReachLimitException(final ReachLimitException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error handleConflictException(final ConflictException exc) {
        log.error("Отловлена ошибка: " + exc.getMessage());
        String status = HttpStatus.CONFLICT.getReasonPhrase();
        String reason = "Для запрошенной операции не выполняются необходимые условия";
        return new Error(status, reason, exc.getMessage(), LocalDateTime.now().withNano(0));
    }
}
