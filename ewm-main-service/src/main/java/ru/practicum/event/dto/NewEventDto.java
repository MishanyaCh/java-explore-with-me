package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class NewEventDto {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotBlank(message = "Поле 'title' не должно быть пустым и состоять из пробелов")
    @Size(min = 3, max = 120, message = "Минимальное количество символов в поле 'title' 3, а максимальное - 120")
    private String title; // заголовок события

    @JsonProperty(value = "annotation")
    @NotBlank(message = "Поле 'annotation' не должно быть пустым и состоять из пробелов")
    @Size(min = 20, max = 2000, message = "Минимальное количество символов в поле 'annotation' 20, а максимальное - 2000")
    private String shortDescription;

    @JsonProperty(value = "description")
    @NotBlank(message = "Поле 'description' не должно быть пустым и состоять из пробелов")
    @Size(min = 20, max = 7000, message = "Минимальное количество символов в поле 'description' 20, а максимальное - 7000")
    private String fullDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime eventDate;

    @JsonProperty(value = "category")
    private Integer categoryId;
    private Boolean paid; // нужно ли оплачивать участие в событии

    @PositiveOrZero(message = "Значение в поле 'participantLimit' не должно быть меньше нуля")
    private Integer participantLimit; // ограничение на кол-во участников
    private Boolean requestModeration; // нужна ли пре-модерация заявок.

    @JsonProperty(value = "location")
    @NotNull(message = "Поле 'location' должно присутствовать")
    private LocationDto locationDto; // координаты события

    public NewEventDto(String titleArg, String shortDescriptionArg, String fullDescriptionArg,
                       LocalDateTime eventDateArg, Integer categoryIdArg, Boolean paidArg, Integer participantLimitArg,
                       Boolean requestModerationArg, LocationDto locationDtoArg) {
        title = titleArg;
        shortDescription = shortDescriptionArg;
        fullDescription = fullDescriptionArg;
        eventDate = eventDateArg;
        categoryId = categoryIdArg;
        paid = paidArg;
        participantLimit = participantLimitArg;
        requestModeration = requestModerationArg;
        locationDto = locationDtoArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "NewEventDto{" + "title='" + title + '\'' +
                ", shortDescription.length=" + shortDescription.length() +
                ", fullDescription.length=" + fullDescription.length() +
                ", eventDate=" + eventDate.format(formatter) +
                ", categoryId=" + categoryId + ", paid=" + paid + ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration + ", " + locationDto + "}";
    }
}
