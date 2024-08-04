package ru.practicum.participationRequest.service;

import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusResult;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto createParticipationRequest(int userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(int userId, long requestId);

    List<ParticipationRequestDto> getParticipationRequestsList(int userId);
}
