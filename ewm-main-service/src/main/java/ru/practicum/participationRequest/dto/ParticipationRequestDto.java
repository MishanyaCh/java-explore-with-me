package ru.practicum.participationRequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ParticipationRequestDto {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private Long id;
    private String status;

    @JsonProperty(value = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime creationDate;

    @JsonProperty(value = "event")
    private Long eventId;

    @JsonProperty(value = "requester")
    private Integer requesterId;

    public ParticipationRequestDto(Long idArg, String statusArg, LocalDateTime creationDateArg, Long eventIdArg,
                                   Integer requesterIdArg) {
        id = idArg;
        status = statusArg;
        creationDate = creationDateArg;
        eventId = eventIdArg;
        requesterId = requesterIdArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "ParticipationRequestDto{" + "id=" + id + ", status='" + status + '\'' +
                ", creationDate=" + creationDate.format(formatter) +
                ", eventId=" + eventId + ", requesterId=" + requesterId + "}";
    }
}
