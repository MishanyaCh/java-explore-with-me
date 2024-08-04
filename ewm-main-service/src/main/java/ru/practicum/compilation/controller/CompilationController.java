package ru.practicum.compilation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
public class CompilationController {
    private static final Logger log = LoggerFactory.getLogger(CompilationController.class);
    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationServiceArg) {
        compilationService = compilationServiceArg;
    }

    @PostMapping(path = "/admin/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@Valid @RequestBody NewCompilationDto inputDto) {
        log.info("Пришел POST /admin/compilation запрос с телом: {}", inputDto);
        final CompilationResponseDto createdCompilation = compilationService.createCompilationByAdmin(inputDto);
        log.info("На POST /admin/compilation запрос отправлен ответ с телом: {}", createdCompilation);
        return createdCompilation;
    }

    @PatchMapping(path = "/admin/compilations/{compId}")
    public CompilationResponseDto updateCompilation(
            @PathVariable long compId, @Valid @RequestBody UpdateCompilationRequestDto updatedCompilationDto) {
        log.info("Пришел PATCH /admin/compilations/{} запрос с телом: {}", compId, updatedCompilationDto);
        final CompilationResponseDto updatedCompilation = compilationService
                .updateCompilationByAdmin(compId, updatedCompilationDto);
        log.info("На PATCH /admin/compilations/{} запрос отправлен ответ с телом: {}", compId, updatedCompilation);
        return updatedCompilation;
    }

    @DeleteMapping(path = "/admin/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Пришел DELETE /admin/compilations/{} запрос", compId);
        compilationService.deleteCompilationByAdmin(compId);
        log.info("На DELETE /admin/compilations/{} запрос отправлен ответ без тела", compId);
    }

    @GetMapping(path = "/compilations/{compId}")
    public CompilationResponseDto getCompilation(@PathVariable long compId) {
        log.info("Пришел GET /compilations/{} запрос", compId);
        final CompilationResponseDto compilation = compilationService.getCompilation(compId);
        log.info("На GET /compilations/{} запрос отправлен ответ с телом: {}", compId, compId);
        return compilation;
    }

    @GetMapping(path = "/compilations")
    public List<CompilationResponseDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("Пришел GET /compilations?pinned={}&from={}&size={} запрос", pinned, from, size);
        final List<CompilationResponseDto> compilations = compilationService.getCompilations(pinned, from, size);
        log.info("На GET /compilations?pinned={}&from={}&size={} запрос отправлен ответ с размером телом: {}",
                pinned, from, size, compilations.size());
        return compilations;
    }
}
