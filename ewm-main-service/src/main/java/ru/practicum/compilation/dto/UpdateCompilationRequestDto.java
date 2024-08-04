package ru.practicum.compilation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;

@Getter
@Setter
public class UpdateCompilationRequestDto {
    @Size(min = 1, max = 50, message = "Максимальное количество символов в поле 'title' 50")
    private String title;

    @JsonProperty(value = "pinned")
    private Boolean isPinned;

    @JsonProperty(value = "events")
    private ArrayList<Long> eventsIds;

    public UpdateCompilationRequestDto (String titleArg, Boolean isPinnedArg, ArrayList<Long> eventsIdsArg) {
        title = titleArg;
        isPinned = isPinnedArg;
        eventsIds = eventsIdsArg;
    }

    @Override
    public String toString() {
        String result = "UpdateCompilationRequestDto{" + "title='" + title + '\'' + ", isPinned=" + isPinned;
        if (eventsIds == null) {
            result = result + ", eventIds=null";
        } else {
            result = result + ", eventsIds=" + eventsIds.toString();
        }
        return result + "}";
    }
}
