package ru.practicum.event.service;

import ru.practicum.enums.EventState;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventResponseDto createEvent(int userId, NewEventDto inputDto);

    EventResponseDto updateEventByEventCreator(int userId, long eventId, UpdateEventDto updatedEventDto);

    List<EventShortResponseDto> getEventsByRegisteredUser(int userId, int from, int size);

    EventFullResponseDto getEventByRegisteredUser(int userId, long eventId);

    List<ParticipationRequestDto> getParticipationRequestsByRegisteredUser(int userId, long eventId);

    UpdateParticipationRequestStatusResult updateParticipationRequestsStatus(
            int userId, long eventId, UpdateParticipationRequestStatusDto updateRequestStatusDto);

    EventFullResponseDto updateEventByAdmin(long eventId, UpdateEventDto updateEventDto);

    List<EventFullResponseDto> searchEventsByAdmin(List<Integer> userIds, List<EventState> eventStates,
                                                   List<Integer> categoryIds, String rangeStart, String rangeEnd,
                                                   int from, int size);

    EventFullResponseDto getEvent(long eventId, HttpServletRequest servletRequest);

    List<EventShortResponseDto> searchEvents(String text, List<Integer> categoryIds, Boolean paid,
                                                       String rangeStart, String rangeEnd,
                                                       Boolean onlyAvailable, String sort, int from, int size,
                                                       HttpServletRequest servletRequest);
}

