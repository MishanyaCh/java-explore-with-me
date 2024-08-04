package ru.practicum.event.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusResult;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private static final Logger log = LoggerFactory.getLogger(EventPrivateController.class);
    private final EventService eventService;

    @Autowired
    public EventPrivateController(EventService eventServiceArg) {
        eventService = eventServiceArg;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventResponseDto createEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto inputDto) {
        log.info("Пришел POST /users/{}/events запрос с телом: {}", userId, inputDto);
        final EventResponseDto createEvent = eventService.createEvent(userId, inputDto);
        log.info("На POST /users/{}/events запрос отправлен ответ с телом: {}", userId, createEvent);
        return createEvent;
    }

    @PatchMapping(path = "/{eventId}")
    public EventResponseDto updateEvent(@PathVariable Integer userId, @PathVariable Long eventId,
                                        @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Пришел PATCH /users/{}/events/{} запрос с телом: {}", userId, eventId, updateEventDto);
        final EventResponseDto updatedEvent = eventService.updateEventByEventCreator(userId, eventId, updateEventDto);
        log.info("На PATCH /users/{}/events/{} запрос отправлен ответ с телом: {}", userId, eventId, updatedEvent);
        return updatedEvent;
    }

    @GetMapping(path = "/{eventId}")
    public EventFullResponseDto getEvent(@PathVariable Integer userId, @PathVariable Long eventId) {
        log.info("Пришел GET /users/{}/events/{} запрос", userId, eventId);
        final EventFullResponseDto event = eventService.getEventByRegisteredUser(userId, eventId);
        log.info("На GET /users/{}/events/{} запрос отправлен ответ с телом: {}", userId, eventId, event);
        return event;
    }

    @GetMapping
    public List<EventShortResponseDto> getEventsList(
            @PathVariable Integer  userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("Пришел GET /users/{}/events?from={}&size={} запрос", userId, from, size);
        final List<EventShortResponseDto> result = eventService.getEventsByRegisteredUser(userId, from, size);
        log.info("На GET /users/{}/events?from={}&size={} запрос отправлен ответ с размером тела: {}",
                userId, from, size, result.size());
        return result;
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsList(@PathVariable Integer userId,
                                                                      @PathVariable Long eventId) {
        log.info("Пришел GET /users/{}/events/{}/requests запрос", userId, eventId);
        final List<ParticipationRequestDto> result = eventService
                .getParticipationRequestsByRegisteredUser(userId, eventId);
        log.info("На /users/{}/events/{}/requests запрос отправлен ответ с размером тела: {}",
                userId, eventId, result.size());
        return result;
    }

    @PatchMapping(path = "/{eventId}/requests")
    public UpdateParticipationRequestStatusResult updateParticipationRequestsStatus(
            @PathVariable Integer userId, @PathVariable Long eventId,
            @RequestBody UpdateParticipationRequestStatusDto updatedParticipationRequestStatusDto) {
        log.info("Пришел PATCH /users/{}/events/{}/requests запрос с телом: {}", userId, eventId,
                updatedParticipationRequestStatusDto);
        final UpdateParticipationRequestStatusResult result = eventService
                .updateParticipationRequestsStatus(userId, eventId, updatedParticipationRequestStatusDto);
        log.info("На PATCH /users/{}/events/{}/requests запрос отправлен ответ с телом: {}", userId, eventId, result);
        return result;
    }
}
