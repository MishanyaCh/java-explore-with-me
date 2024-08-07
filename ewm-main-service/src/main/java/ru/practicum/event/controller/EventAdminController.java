package ru.practicum.event.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.enums.EventState;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private static final Logger log = LoggerFactory.getLogger(EventAdminController.class);
    private final EventService eventService;

    @Autowired
    public EventAdminController(EventService eventServiceArg) {
        eventService = eventServiceArg;
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullResponseDto updateEvent(@PathVariable long eventId,
                                            @Valid @RequestBody UpdateEventDto updatedEventDto) {
        log.info("Пришел PATCH /admin/events/{} запрос с телом: {}", eventId, updatedEventDto);
        final EventFullResponseDto updatedEvent = eventService.updateEventByAdmin(eventId, updatedEventDto);
        log.info("На PATCH /admin/events/{} запрос отправлен ответ с телом: {}", eventId, updatedEvent);
        return updatedEvent;
    }

    @GetMapping
    public List<EventFullResponseDto> getEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("Пришел GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}" +
                "&from={}&size={} запрос", users, states, categories, rangeStart, rangeEnd, from, size);
        final List<EventFullResponseDto> result = eventService.searchEventsByAdmin(
                users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("На GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={} " +
                        "запрос отправлен ответ с размером тела: {}", users, states, categories, rangeStart, rangeEnd,
                from, size, result.size());
        return result;
    }
}
