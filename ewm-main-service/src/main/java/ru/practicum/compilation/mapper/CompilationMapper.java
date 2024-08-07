package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationMapper {
    Compilation toCompilation(NewCompilationDto inputDto);

    CompilationResponseDto toCompilationDto(Compilation compilation);

    Compilation toUpdatedCompilation(UpdateCompilationRequestDto updatedCompilationDto);

    List<CompilationResponseDto> toCompilationDtoList(List<Compilation> compilations);
}
