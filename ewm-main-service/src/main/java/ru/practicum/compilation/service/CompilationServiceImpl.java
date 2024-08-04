package ru.practicum.compilation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.util.Pages;

import java.util.List;
import java.util.Optional;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;

    @Autowired
    public CompilationServiceImpl(CompilationMapper compilationMapperArg,
                                  CompilationRepository compilationRepositoryArg) {
        compilationMapper =  compilationMapperArg;
        compilationRepository = compilationRepositoryArg;
    }

    @Override
    public CompilationResponseDto createCompilationByAdmin(NewCompilationDto inputDto) {
        Compilation newCompilation = compilationMapper.toCompilation(inputDto);
        Compilation createdCompilation = compilationRepository.save(newCompilation);
        return compilationMapper.toCompilationDto(createdCompilation);
    }

    @Override
    public CompilationResponseDto updateCompilationByAdmin(
            long compilationId, UpdateCompilationRequestDto updatedCompilationDto) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compilationId);
        if (optionalCompilation.isEmpty()) {
            String message = String.format("Подборка с id=%d не найдена! Операция обновления невозможна", compilationId);
            throw new ObjectNotFoundException(message);
        }
        Compilation savedCompilation = optionalCompilation.get();

        Compilation updatedCompilation = compilationMapper.toUpdatedCompilation(updatedCompilationDto);
        Compilation updatedFiledsForSavedCompilation = updateCompilationFields(savedCompilation, updatedCompilation);
        Compilation updatedInDB = compilationRepository.save(updatedFiledsForSavedCompilation);
        return compilationMapper.toCompilationDto(updatedInDB);
    }

    @Override
    public void deleteCompilationByAdmin(long compilationId) {
        boolean isCompilationExist = compilationRepository.existsById(compilationId);
        if (!isCompilationExist) {
            String message = String.format("Подборка с id=%d не найдена! Операция удаления невозможна", compilationId);
            throw new ObjectNotFoundException(message);
        }
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationResponseDto getCompilation(long compilationId) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compilationId);
        if (optionalCompilation.isEmpty()) {
            String message = String.format("Подборка с id=%d не найдена!", compilationId);
            throw new ObjectNotFoundException(message);
        }
        Compilation compilation = optionalCompilation.get();
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationResponseDto> getCompilations(Boolean isPinned, int from, int size) {
        Pageable pageable = Pages.getPage(from, size);
        List<Compilation> compilations;
        if (isPinned == null) {
            Page<Compilation> compilationPage = compilationRepository.findAll(pageable);
            compilations = compilationPage.getContent();
        } else {
            compilations = compilationRepository.findCompilationsByIsPinned(isPinned, pageable);
        }
        return compilationMapper.toCompilationDtoList(compilations);
    }

    private Compilation updateCompilationFields(Compilation savedCompilation, Compilation updatedCompilationData) {
        String updatedTitle = updatedCompilationData.getTitle();
        Boolean updatedIsPinned = updatedCompilationData.getIsPinned();
        List<Event> eventsForUpdatedCompilation = updatedCompilationData.getEvents();

        if (updatedTitle != null) {
            savedCompilation.setTitle(updatedTitle);
        }
        if (updatedIsPinned != null) {
            savedCompilation.setIsPinned(updatedIsPinned);
        }
        if (!eventsForUpdatedCompilation.isEmpty()) {
            savedCompilation.setEvents(eventsForUpdatedCompilation);
        }
        return savedCompilation;
    }
}
