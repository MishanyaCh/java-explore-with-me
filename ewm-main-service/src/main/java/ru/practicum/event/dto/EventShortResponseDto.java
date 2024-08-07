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
public class EventShortResponseDto {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private Long id;
    private String title;

    @JsonProperty(value = "annotation")
    private String shortDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer confirmedRequests; // кол-во одобренных заявок на участие в событии
    private Integer views; // кол-во просмотров события

    @JsonProperty(value = "category")
    private CategoryDto categoryDto;

    @JsonProperty(value = "initiator")
    private UserShortDto userShortDto;

    public EventShortResponseDto(Long idArg, String titleArg, String shortDescriptionArg, LocalDateTime eventDateArg,
                                 Boolean paidArg, Integer confirmedRequestsArg, Integer viewsArg, CategoryDto categoryDtoArg,
                                 UserShortDto userShortDtoArg) {
        id = idArg;
        title = titleArg;
        shortDescription = shortDescriptionArg;
        eventDate = eventDateArg;
        paid = paidArg;
        confirmedRequests = confirmedRequestsArg;
        views = viewsArg;
        categoryDto = categoryDtoArg;
        userShortDto = userShortDtoArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "EventShortResponseDto{" + "id=" + id + ", title='" + title + '\'' +
                ", shortDescription.length=" + shortDescription.length() +
                ", eventDate=" + eventDate.format(formatter) + ", paid=" + paid +
                ", confirmedRequests=" + confirmedRequests + ", views=" + views +
                ", " + categoryDto + ", " + userShortDto + "}";
    }
}
