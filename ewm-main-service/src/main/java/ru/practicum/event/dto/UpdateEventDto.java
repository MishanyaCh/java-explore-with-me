package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class UpdateEventDto {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    @Size(min = 3, max = 120, message = "Минимальное количество символов в поле 'title' 3, а максимальное - 120")
    private String title;

    @JsonProperty(value = "annotation")
    @Size(min = 20, max = 2000, message = "Минимальное количество символов в поле 'annotation' 20, а максимальное - 2000")
    private String shortDescription;

    @JsonProperty(value = "description")
    @Size(min = 20, max = 7000, message = "Минимальное количество символов в поле 'description' 20, а максимальное - 7000")
    private String fullDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime eventDate;

    @JsonProperty(value = "category")
    private Integer categoryId;
    private Boolean paid;

    @PositiveOrZero(message = "Значение в поле 'participantLimit' не должно быть меньше нуля")
    private Integer participantLimit;
    private Boolean requestModeration;

    @JsonProperty(value = "location")
    private LocationDto locationDto;

    private String stateAction;

    public UpdateEventDto(String titleArg, String shortDescriptionArg, String fullDescriptionArg,
                          LocalDateTime eventDateArg, Integer categoryIdArg, Boolean paidArg,
                          Integer participantLimitArg, Boolean requestModerationArg,
                          LocationDto locationDtoArg, String stateActionArg) {
        title = titleArg;
        shortDescription = shortDescriptionArg;
        fullDescription = fullDescriptionArg;
        eventDate = eventDateArg;
        categoryId = categoryIdArg;
        paid = paidArg;
        participantLimit = participantLimitArg;
        requestModeration = requestModerationArg;
        locationDto = locationDtoArg;
        stateAction = stateActionArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String result = "UpdateEventDto{" + "title='" + title + '\'' + ", categoryId=" + categoryId +
                ", paid=" + paid + ", participantLimit=" + participantLimit + ", requestModeration=" + requestModeration;

        if (shortDescription == null) {
            result = result + ", shortDescription.length=null";
        } else {
            result = result + ", shortDescription.length=" + shortDescription.length();
        }
        if (fullDescription == null) {
            result = result + ", fullDescription.length=null";
        } else {
            result = result +  ", fullDescription.length=" + fullDescription.length();
        }
        if (eventDate == null) {
            result = result + ", eventDate=null";
        } else {
            result = result + ", eventDate=" + eventDate.format(formatter);
        }
        if (locationDto == null) {
            result = result + ", LocationDto{latitude=null, longitude=null}";
        } else {
            result = result + ", " + locationDto;
        }
        return result + ", stateAction='" + stateAction + '\'' + "}";
    }
}
