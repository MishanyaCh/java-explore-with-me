package ru.practicum.enums;

public enum ParticipationRequestStatus {
    PENDING, // заявка на участие находится на рассмотрении у автора мероприятия
    CONFIRMED, // заявка на участие подтверждена автором
    CANCELED, // заявка на участие отменена участником мероприятия
    REJECTED // заявка отклонена автором мероприятия
}
