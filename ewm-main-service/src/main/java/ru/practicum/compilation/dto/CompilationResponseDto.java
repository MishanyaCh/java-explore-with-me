package ru.practicum.compilation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.dto.EventShortResponseDto;

import java.util.List;

@Getter
@Setter
public class CompilationResponseDto {
    private Long id;
    private String title;

    @JsonProperty(value = "pinned")
    private Boolean isPinned;

    @JsonProperty(value = "events")
    private List<EventShortResponseDto> responseDto;

    public CompilationResponseDto(Long idArg, String titleArg, Boolean isPinnedArg,
                                  List<EventShortResponseDto> responseDtoArg) {
        id = idArg;
        title = titleArg;
        isPinned = isPinnedArg;
        responseDto = responseDtoArg;
    }

    @Override
    public String toString() {
        return "CompilationResponseDto{" + "id=" + id + ", title='" + title + '\'' + ", isPinned=" + isPinned +
                ", " + responseDto.toString() + "}";
    }
}
