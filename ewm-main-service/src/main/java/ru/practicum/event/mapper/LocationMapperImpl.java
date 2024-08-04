package ru.practicum.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

@Component
public class LocationMapperImpl implements LocationMapper {
    @Override
    public Location toLocation(LocationDto inputDto) {
        float latitude = inputDto.getLatitude();
        float longitude = inputDto.getLongitude();
        return new Location(null, latitude, longitude);
    }

    @Override
    public LocationDto toLocationDto(Location location) {
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        return new LocationDto(latitude, longitude);
    }
}
