package ru.practicum.exception;

public class ReachLimitException extends RuntimeException {
    public ReachLimitException(String message) {
        super(message);
    }
}
