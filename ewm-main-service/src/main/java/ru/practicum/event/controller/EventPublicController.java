package ru.practicum.event.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/events")
public class EventPublicController {
    private static final Logger log = LoggerFactory.getLogger(EventPublicController.class);
    private final EventService eventService;

    @Autowired
    public EventPublicController(EventService eventServiceArg) {
        eventService = eventServiceArg;
    }

    @GetMapping(path = "/{id}")
    public EventFullResponseDto getEvent(@PathVariable long id, HttpServletRequest request) {
        log.info("Пришел GET /events/{} запрос", id);
        final EventFullResponseDto responseDto = eventService.getEvent(id, request);
        log.info("На GET /events/{} запрос отправлен ответ с телом: {}",  id, responseDto);
        return responseDto;
    }

    @GetMapping
    public List<EventShortResponseDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size, HttpServletRequest request) {
        log.info("Пришел GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&" +
                "sort={}&from={}&size={} запрос", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size);
        final List<EventShortResponseDto> result = eventService
                .searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        log.info("На GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&" +
                "from={}&size={} запрос отправлен ответ с размером тела: {}", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, result.size());
        return result;
    }
}
