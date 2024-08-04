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
public class EventResponseDto {
    protected static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private Long id;
    private String title;

    @JsonProperty(value = "annotation")
    private String shortDescription;

    @JsonProperty(value = "description")
    private String fullDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime eventDate;

    @JsonProperty(value = "createdOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime creationDate;
    private String state;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;

    @JsonProperty(value = "category")
    private CategoryDto categoryDto;

    @JsonProperty(value = "location")
    private LocationDto locationDto;

    @JsonProperty(value = "initiator")
    private UserShortDto userShortDto;

    public EventResponseDto(Long idArg, String titleArg, String shortDescriptionArg, String fullDescriptionArg,
                            LocalDateTime eventDateArg, LocalDateTime creationDateArg, String stateArg,
                            Boolean paidArg, Boolean requestModerationArg, Integer participantLimitArg,
                            CategoryDto categoryDtoArg, LocationDto locationDtoArg, UserShortDto userShortDtoArg) {
        id = idArg;
        title = titleArg;
        shortDescription = shortDescriptionArg;
        fullDescription = fullDescriptionArg;
        eventDate = eventDateArg;
        creationDate = creationDateArg;
        state = stateArg;
        paid = paidArg;
        requestModeration = requestModerationArg;
        participantLimit = participantLimitArg;
        categoryDto = categoryDtoArg;
        locationDto = locationDtoArg;
        userShortDto = userShortDtoArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "EventResponseDto{" + "id=" + id + ", title='" + title + '\'' +
                ", shortDescription.length=" + shortDescription.length() +
                ", fullDescription.length=" + fullDescription.length() +
                ", eventDate=" + eventDate.format(formatter) + ", creationDate=" + creationDate.format(formatter) +
                ", state='" + state + '\'' + ", paid=" + paid + ", requestModeration=" + requestModeration +
                ", participantLimit=" + participantLimit + ", " +
                categoryDto + ", " + locationDto + ", " + userShortDto + "}";
    }
}
