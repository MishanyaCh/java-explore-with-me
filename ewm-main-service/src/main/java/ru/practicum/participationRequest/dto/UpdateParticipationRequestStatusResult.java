package ru.practicum.participationRequest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateParticipationRequestStatusResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public UpdateParticipationRequestStatusResult(List<ParticipationRequestDto> confirmedRequestsArg,
                                                  List<ParticipationRequestDto> rejectedRequestsArg) {
        confirmedRequests = confirmedRequestsArg;
        rejectedRequests = rejectedRequestsArg;
    }

    @Override
    public String toString() {
        return "UpdateParticipationRequestStatusResult{" + confirmedRequests.toString() + ", " + rejectedRequests + "}";
    }
}
