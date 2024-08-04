package ru.practicum.compilation.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationResponseDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompilationMapperImpl implements CompilationMapper {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public CompilationMapperImpl(EventRepository eventRepositoryArg, EventMapper eventMapperArg) {
        eventRepository = eventRepositoryArg;
        eventMapper = eventMapperArg;
    }

    @Override
    public Compilation toCompilation(NewCompilationDto inputDto) {
        String title = inputDto.getTitle();
        Boolean isPinned = inputDto.getIsPinned();
        List<Long> eventIds = inputDto.getEventIds();
        List<Event> savedEvents;
        if (isPinned == null) {
            isPinned = false;
        }
        if (eventIds != null) {
            savedEvents = eventRepository.findAllById(eventIds); // находим список сохраненных событий
        } else {
            savedEvents = new ArrayList<>();
        }
        return new Compilation(null, title, isPinned, savedEvents);
    }

    @Override
    public CompilationResponseDto toCompilationDto(Compilation compilation) {
        Long id = compilation.getId();
        String title = compilation.getTitle();
        Boolean isPinned = compilation.getIsPinned();
        List<Event> events = compilation.getEvents();
        List<EventShortResponseDto> result = eventMapper.toEventShortDtoList(events);
        return new CompilationResponseDto(id, title, isPinned, result);
    }

    @Override
    public Compilation toUpdatedCompilation(UpdateCompilationRequestDto updatedCompilationDto) {
        String updatedTitle = updatedCompilationDto.getTitle();
        Boolean updatedIsPinned = updatedCompilationDto.getIsPinned();
        List<Long> updatedEventIds = updatedCompilationDto.getEventsIds();
        List<Event> updatedEvents;
        if (updatedEventIds != null) {
            updatedEvents = eventRepository.findAllById(updatedEventIds);
        } else {
            updatedEvents = new ArrayList<>();
        }
        return new Compilation(null, updatedTitle, updatedIsPinned, updatedEvents);
    }

    @Override
    public List<CompilationResponseDto> toCompilationDtoList(List<Compilation> compilations) {
        List<CompilationResponseDto> result = new ArrayList<>();
        if (compilations.isEmpty()) {
            return result;
        }
        for (Compilation compilation : compilations) {
            CompilationResponseDto responseDto = toCompilationDto(compilation);
            result.add(responseDto);
        }
        return result;
    }
}
