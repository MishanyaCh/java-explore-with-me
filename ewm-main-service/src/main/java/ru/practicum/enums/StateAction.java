package ru.practicum.enums;

import ru.practicum.exception.UnknownStateActionException;

public enum StateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static StateAction convertToEnum(String stateAction) {
        String text = stateAction.toUpperCase();
        try {
            return StateAction.valueOf(text);
        } catch (IllegalArgumentException exc) {
            String message = String.format("Unknown stateAction: %s", text);
            throw new UnknownStateActionException(message);
        }
    }
}
