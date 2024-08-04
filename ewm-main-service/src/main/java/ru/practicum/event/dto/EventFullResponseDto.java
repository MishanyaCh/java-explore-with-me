package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class EventFullResponseDto extends EventResponseDto {
    @JsonProperty(value = "publishedOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime publicationDate;
    private Integer confirmedRequests;
    private Integer views;

    public EventFullResponseDto(Long idArg, String titleArg, String shortDescriptionArg, String fullDescriptionArg,
                                LocalDateTime eventDateArg, LocalDateTime creationDateArg, String stateArg,
                                Boolean paidArg, Boolean requestModerationArg, Integer participantLimitArg,
                                CategoryDto categoryDtoArg, LocationDto locationDtoArg, UserShortDto userShortDtoArg,
                                LocalDateTime publicationDateArg, Integer confirmedRequestsArg, Integer viewsArg) {
        super(idArg, titleArg, shortDescriptionArg, fullDescriptionArg, eventDateArg, creationDateArg, stateArg,
                paidArg, requestModerationArg, participantLimitArg, categoryDtoArg, locationDtoArg, userShortDtoArg);
        publicationDate = publicationDateArg;
        confirmedRequests = confirmedRequestsArg;
        views = viewsArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String result = "EventFullResponseDto{" + "id=" + getId() + ", title='" + getTitle() + '\'' +
                ", shortDescription.length=" + getShortDescription().length() +
                ", fullDescription.length=" + getFullDescription().length() +
                ", eventDate=" + getEventDate().format(formatter) +
                ", creationDate=" + getCreationDate().format(formatter) + ", state='" + getState() + '\'' +
                ", paid=" + getPaid() + ", requestModeration=" + getRequestModeration() +
                ", participantLimit=" + getParticipantLimit();
        if (publicationDate == null) {
            result = result + ", publicationDate=null";
        } else {
            result = result + ", publicationDate=" + publicationDate.format(formatter);
        }
        return result + ", confirmedRequest=" + confirmedRequests + ", views=" + views + ", " +
                getCategoryDto() + ", " + getLocationDto() + ", " + getUserShortDto() + "}";
    }
}
