package ru.practicum.event.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.enums.EventState;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.storage.LocationRepository;
import ru.practicum.exception.EventDateTimeException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EventMapperImpl implements EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Autowired
    public EventMapperImpl(UserMapper userMapperArg,
                           CategoryMapper categoryMapperArg, CategoryRepository categoryRepositoryArg,
                           LocationMapper locationMapperArg, LocationRepository locationRepositoryArg) {
        userMapper = userMapperArg;
        categoryMapper = categoryMapperArg;
        categoryRepository = categoryRepositoryArg;
        locationMapper = locationMapperArg;
        locationRepository = locationRepositoryArg;
    }

    @Override
    public Event toEvent(NewEventDto inputDto) {
        String title = inputDto.getTitle();
        String shortDescription = inputDto.getShortDescription();
        String fullDescription = inputDto.getFullDescription();
        LocalDateTime eventDate = inputDto.getEventDate();
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        Boolean isPaid = inputDto.getPaid();
        Boolean isRequestModeration = inputDto.getRequestModeration();
        Integer participantLimit = inputDto.getParticipantLimit();

        Integer categoryId = inputDto.getCategoryId();
        Category category = checkCategoryExistence(categoryId); // проверяем, что в таблице categories есть такая категория
        LocationDto locationDto = inputDto.getLocationDto();
        Location location = searchOrSaveLocation(locationDto); // проверяем, что в таблице locations есть такая локация, а если нет, то добавляем

        if (isPaid == null) {
            isPaid = false;
        }
        if (isRequestModeration == null) {
            isRequestModeration = true;
        }
        if (participantLimit == null) {
            participantLimit = 0;
        }

        checkEventDate(eventDate);
        return new Event(null, title, shortDescription, fullDescription, eventDate, creationDate,
                EventState.PENDING, isPaid, isRequestModeration, participantLimit, null,
                0, 0, category, location, null);
    }

    @Override
    public Event toUpdatedEvent(UpdateEventDto updatedEventDto) {
        Category updatedCategory = null;
        Location updatedLocation = null;

        String title = updatedEventDto.getTitle();
        String shortDescription = updatedEventDto.getShortDescription();
        String fullDescription = updatedEventDto.getFullDescription();
        LocalDateTime eventDate = updatedEventDto.getEventDate();
        Boolean isPaid = updatedEventDto.getPaid();
        Boolean isRequestModeration = updatedEventDto.getRequestModeration();
        Integer participantLimit = updatedEventDto.getParticipantLimit();
        Integer categoryId = updatedEventDto.getCategoryId();
        LocationDto locationDto = updatedEventDto.getLocationDto();

        if (categoryId != null) {
            updatedCategory = checkCategoryExistence(categoryId);
        }
        if (locationDto != null) {
            updatedLocation = searchOrSaveLocation(locationDto);
        }

        return new Event(null, title, shortDescription, fullDescription, eventDate, null,
                null, isPaid, isRequestModeration, participantLimit, null, 0,
                0, updatedCategory, updatedLocation, null);
    }

    @Override
    public EventResponseDto toEventDto(Event event) {
        Long id = event.getId();
        String title = event.getTitle();
        String shortDescription = event.getShortDescription();
        String fullDescription = event.getFullDescription();
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime creationDate = event.getCreationDate();
        String state = event.getState().toString();
        Boolean isPaid = event.getPaid();
        Boolean isRequestModeration = event.getRequestModeration();
        Integer participantLimit = event.getParticipantLimit();

        Category category = event.getCategory();
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        Location location = event.getLocation();
        LocationDto locationDto = locationMapper.toLocationDto(location);
        User user = event.getCreator();
        UserShortDto userShortDto = userMapper.toUserShortDto(user);

        return new EventResponseDto(id, title, shortDescription, fullDescription, eventDate, creationDate, state,
                isPaid, isRequestModeration, participantLimit, categoryDto, locationDto, userShortDto);
    }

    @Override
    public EventShortResponseDto toEventShortDto(Event event) {
        Long id = event.getId();;
        String title = event.getTitle();
        String shortDescription = event.getShortDescription();
        LocalDateTime eventDate = event.getEventDate();
        Boolean paid = event.getPaid();
        Integer confirmedRequests = event.getConfirmedRequests();
        Integer views = event.getViews();

        Category category = event.getCategory();
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        User user = event.getCreator();
        UserShortDto userShortDto = userMapper.toUserShortDto(user);

        return new EventShortResponseDto(id, title, shortDescription, eventDate, paid, confirmedRequests, views,
                categoryDto, userShortDto);
    }

    @Override
    public List<EventShortResponseDto> toEventShortDtoList(List<Event> events) {
        List<EventShortResponseDto> result = new ArrayList<>();
        if (events.isEmpty()) {
            return result;
        }
        for (Event event : events) {
            EventShortResponseDto shortDto = toEventShortDto(event);
            result.add(shortDto);
        }
        return result;
    }

    @Override
    public EventFullResponseDto toEventFullDto(Event event) {
        Long id = event.getId();
        String title = event.getTitle();
        String shortDescription = event.getShortDescription();
        String fullDescription = event.getFullDescription();
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime creationDate = event.getCreationDate();
        String state = event.getState().toString();
        Boolean isPaid = event.getPaid();
        Boolean isRequestModeration = event.getRequestModeration();
        Integer participantLimit = event.getParticipantLimit();
        LocalDateTime publicationDate = event.getPublicationDate();
        Integer confirmedRequests = event.getConfirmedRequests();
        Integer views = event.getViews();

        Category category = event.getCategory();
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        Location location = event.getLocation();
        LocationDto locationDto = locationMapper.toLocationDto(location);
        User user = event.getCreator();
        UserShortDto userShortDto = userMapper.toUserShortDto(user);

        return new EventFullResponseDto(id, title, shortDescription, fullDescription, eventDate, creationDate,
                state, isPaid, isRequestModeration, participantLimit, categoryDto, locationDto, userShortDto,
                publicationDate, confirmedRequests, views);
    }

    @Override
    public List<EventFullResponseDto> toEventFullDtoList(List<Event> events) {
        List<EventFullResponseDto> result = new ArrayList<>();
        if (events.isEmpty()) {
            return result;
        }
        for (Event event : events) {
            EventFullResponseDto responseDto = toEventFullDto(event);
            result.add(responseDto);
        }
        return result;
    }

    private Category checkCategoryExistence(int catId) {
        Optional<Category> categoryOptional = categoryRepository.findById(catId);
        if (categoryOptional.isEmpty()) {
            String message = String.format("Категория с id=%d не найдена!", catId);
            throw new ObjectNotFoundException(message);
        }
        return categoryOptional.get();
    }

    private Location searchOrSaveLocation(LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        Optional<Location> locationOptional = locationRepository.findLocationByLatitudeAndLongitude(latitude, longitude);
        return locationOptional.orElseGet(() -> locationRepository.save(location));
    }

    private void checkEventDate(LocalDateTime eventDate) {
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);
        if (eventDate.minusHours(2).isBefore(currentDate)) {
            String message = "Событие не может начаться раньше чем через 2 часа";
            throw new EventDateTimeException(message);
        }
    }
}
