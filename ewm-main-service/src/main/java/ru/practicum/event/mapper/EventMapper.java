package ru.practicum.event.mapper;

import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventMapper {
    Event toEvent(NewEventDto inputDto);

    Event toUpdatedEvent(UpdateEventDto updatedEventDto);

    EventResponseDto toEventDto(Event event);

    EventShortResponseDto toEventShortDto(Event event);

    List<EventShortResponseDto> toEventShortDtoList(List<Event> events);

    EventFullResponseDto toEventFullDto(Event event);

    List<EventFullResponseDto> toEventFullDtoList(List<Event> events);
}
