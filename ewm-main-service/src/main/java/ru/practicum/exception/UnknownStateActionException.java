package ru.practicum.exception;

public class UnknownStateActionException extends RuntimeException {
    public UnknownStateActionException(String message) {
        super(message);
    }
}
