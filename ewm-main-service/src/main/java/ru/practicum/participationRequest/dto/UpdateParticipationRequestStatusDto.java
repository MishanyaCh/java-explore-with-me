package ru.practicum.participationRequest.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.enums.ParticipationRequestStatus;

import java.util.ArrayList;

@Getter
@Setter
public class UpdateParticipationRequestStatusDto {
    private ArrayList<Long> requestIds;
    private ParticipationRequestStatus status;

    public UpdateParticipationRequestStatusDto(ArrayList<Long> requestIdsArg, ParticipationRequestStatus statusArg) {
        requestIds = requestIdsArg;
        status = statusArg;
    }

    @Override
    public String toString() {
        return "UpdateParticipationRequestStatusDto{" + "requestIds=" + requestIds.toString() +
                ", status=" + status.toString() + "}";
    }
}
