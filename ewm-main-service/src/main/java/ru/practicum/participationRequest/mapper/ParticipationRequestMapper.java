package ru.practicum.participationRequest.mapper;

import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestMapper {
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> participationRequests);
}
