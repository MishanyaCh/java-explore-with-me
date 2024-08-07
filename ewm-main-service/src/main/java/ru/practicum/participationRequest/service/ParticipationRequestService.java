package ru.practicum.participationRequest.service;

import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto createParticipationRequest(int userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(int userId, long requestId);

    List<ParticipationRequestDto> getParticipationRequestsList(int userId);
}
