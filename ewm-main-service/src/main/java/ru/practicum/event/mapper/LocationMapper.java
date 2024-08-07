package ru.practicum.event.mapper;

import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

public interface LocationMapper {
    Location toLocation(LocationDto inputDto);

    LocationDto toLocationDto(Location location);
}
