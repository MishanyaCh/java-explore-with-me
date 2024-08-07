package ru.practicum.compilation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Getter
@Setter
public class NewCompilationDto {
    @NotBlank(message = "Поле 'title' не должно быть пустым и состоять из пробелов")
    @Size(min = 1, max = 50, message = "Минимальное количество символов в поле 'title' 1, а максимальное - 50")
    private String title;

    @JsonProperty(value = "pinned")
    private Boolean isPinned;

    @JsonProperty(value = "events")
    private ArrayList<Long> eventIds;

    public NewCompilationDto(String titleArg, Boolean isPinnedArg, ArrayList<Long> eventIdsArg) {
        title = titleArg;
        isPinned = isPinnedArg;
        eventIds = eventIdsArg;
    }

    @Override
    public String toString() {
        String result = "NewCompilationDto{" + "title='" + title + '\'' + ", isPinned=" + isPinned;
        if (eventIds == null) {
            result = result + ", eventIds=null";
        } else {
            result = result + ", eventIds=" + eventIds.toString();
        }
        return result + "}";
    }
}
