package ru.practicum.participationRequest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class ParticipationRequestController {
    private static final Logger log = LoggerFactory.getLogger(ParticipationRequestController.class);
    private final ParticipationRequestService requestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService requestServiceArg) {
        requestService = requestServiceArg;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Integer userId, @RequestParam Long eventId) {
        log.info("Пришел POST /users/{}/requests?eventId={} запрос без тела", userId, eventId);
        final ParticipationRequestDto createdRequest = requestService.createParticipationRequest(userId, eventId);
        log.info("На POST /users/{}/requests?eventId={} запрос отправлен ответ с телом: {}",
                userId, eventId, createdRequest);
        return createdRequest;
    }

    @PatchMapping(value = "/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Long requestId) {
        log.info("Пришел PATCH /users/{}/requests/{}/cancel запрос без тела", userId, requestId);
        final ParticipationRequestDto canceledRequest = requestService.cancelParticipationRequest(userId, requestId);
        log.info("На PATCH /users/{}/requests/{}/cancel запрос отправлен ответ с телом: {}",
                userId, requestId, canceledRequest);
        return canceledRequest;
    }

    @GetMapping()
    public List<ParticipationRequestDto> getRequestsList(@PathVariable Integer userId) {
        log.info("Пришел GET /users/{}/requests запрос", userId);
        final List<ParticipationRequestDto> result = requestService.getParticipationRequestsList(userId);
        log.info("На GET /users/{}/requests запрос отправлен ответ с размером тела: {}", userId, result.size());
        return result;
    }
}
