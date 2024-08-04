package ru.practicum.participationRequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParticipationRequestMapperImpl implements ParticipationRequestMapper {
    @Override
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        Long id = participationRequest.getId();
        String status = participationRequest.getStatus().toString();
        LocalDateTime creationDate = participationRequest.getCreationDate();
        Long eventId = participationRequest.getEvent().getId();
        Integer requesterId = participationRequest.getRequester().getId();
        return new ParticipationRequestDto(id, status, creationDate, eventId, requesterId);
    }

    @Override
    public List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> participationRequests) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        if (participationRequests.isEmpty()) {
            return result;
        }
        for (ParticipationRequest request : participationRequests) {
            ParticipationRequestDto participationRequestDto = toParticipationRequestDto(request);
            result.add(participationRequestDto);
        }
        return result;
    }
}
