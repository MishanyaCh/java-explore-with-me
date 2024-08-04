package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {
    CompilationResponseDto createCompilationByAdmin(NewCompilationDto inputDto);

    CompilationResponseDto updateCompilationByAdmin(long compilationId, UpdateCompilationRequestDto updatedCompilationDto);

    void deleteCompilationByAdmin(long compilationId);

    CompilationResponseDto getCompilation(long compilationId);

    List<CompilationResponseDto> getCompilations(Boolean isPinned, int from, int size);
}
