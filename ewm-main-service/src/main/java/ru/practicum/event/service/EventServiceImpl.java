package ru.practicum.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;
import ru.practicum.category.model.Category;
import ru.practicum.client.StatisticClient;
import ru.practicum.enums.EventState;
import ru.practicum.enums.ParticipationRequestStatus;
import ru.practicum.enums.StateAction;
import ru.practicum.event.dto.EventFullResponseDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.EventAlreadyCanceledException;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.exception.EventDateTimeException;
import ru.practicum.exception.NotEventCreatorException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ReachLimitException;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusDto;
import ru.practicum.participationRequest.dto.UpdateParticipationRequestStatusResult;
import ru.practicum.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.storage.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.Pages;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private static final String DATETIMEFORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIMEFORMATTER);

    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;
    private final StatisticClient statisticClient;

    @Autowired
    public EventServiceImpl(EventMapper eventMapperArg, EventRepository eventRepositoryArg,
                            UserRepository userRepositoryArg, ParticipationRequestRepository requestRepositoryArg,
                            ParticipationRequestMapper requestMapperArg, StatisticClient statisticClientArg) {
        eventMapper = eventMapperArg;
        eventRepository = eventRepositoryArg;
        userRepository = userRepositoryArg;
        requestRepository = requestRepositoryArg;
        requestMapper = requestMapperArg;
        statisticClient = statisticClientArg;
    }

    @Override
    public EventResponseDto createEvent(int userId, NewEventDto inputDto) {
        Event newEvent = eventMapper.toEvent(inputDto);
        User eventCreator = checkUserExistence(userId);
        newEvent.setCreator(eventCreator);
        Event createdEvent = eventRepository.save(newEvent);
        return eventMapper.toEventDto(createdEvent);
    }

    @Override
    public EventResponseDto updateEventByEventCreator(int userId, long eventId,
                                                      UpdateEventDto updatedEventDto) {
        Event savedEvent = checkEventExistence(eventId);
        int creatorId = savedEvent.getCreator().getId();
        EventState state = savedEvent.getState();

        if (creatorId != userId) {
            String message = String.format("Пользователь с id=%d не является автором события с id=%d! " +
                    "Невозможно обновить данные этого события.", userId, eventId);
            throw new NotEventCreatorException(message);
        }
        if (state.equals(EventState.PUBLISHED)) {
            String message = String.format("Событие с id=%d уже опубликовано! " +
                    "Обновить данные можно либо у отмененного события, либо у события, ожидающего модерацию.", eventId);
            throw new EventAlreadyPublishedException(message);
        }
        if (updatedEventDto.getEventDate() != null) {
            LocalDateTime updatedEventDate = updatedEventDto.getEventDate();
            LocalDateTime currentDate = LocalDateTime.now().withNano(0);
            if (updatedEventDate.minusHours(2).isBefore(currentDate)) {
                String message = String.format("Событие c id=%d не может начаться раньше чем через 2 часа", eventId);
                throw new EventDateTimeException(message);
            }
            savedEvent.setEventDate(updatedEventDate);
        }
        if (updatedEventDto.getStateAction() != null) {
            String stateAction = updatedEventDto.getStateAction();
            StateAction st = StateAction.convertToEnum(stateAction);

            if (st.equals(StateAction.CANCEL_REVIEW)) {
                savedEvent.setState(EventState.CANCELED);
            } else if (st.equals(StateAction.SEND_TO_REVIEW)) {
                savedEvent.setState(EventState.PENDING);
            }
        }

        Event event = eventMapper.toUpdatedEvent(updatedEventDto);
        Event updatedEvent = updateEventFields(savedEvent, event);
        Event updatedInDB = eventRepository.save(updatedEvent);
        return eventMapper.toEventDto(updatedInDB);
    }

    @Override
    public List<EventShortResponseDto> getEventsByRegisteredUser(int userId, int from, int size) {
        checkUserExistence(userId);
        Pageable pageable = Pages.getPage(from, size);
        List<Event> events = eventRepository.findAllEventsByCreatorId(userId, pageable);
        return eventMapper.toEventShortDtoList(events);
    }

    @Override
    public EventFullResponseDto getEventByRegisteredUser(int userId, long eventId) {
        Event event = checkEventExistence(eventId);
        int creatorId = event.getCreator().getId();
        if (creatorId != userId) {
            String message = String.format("Пользователь с id=%d не является автором события с id=%d! " +
                    "Невозможно получить данные запрошенного события.", userId, eventId);
            throw new NotEventCreatorException(message);
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByRegisteredUser(int userId, long eventId) {
        Event event = checkEventExistence(eventId);
        int creatorId = event.getCreator().getId();
        if (creatorId != userId) {
            String message = String.format("Пользователь с id=%d не является автором события с id=%d! " +
                    "Невозможно получить данные запрошенного события.", userId, eventId);
            throw new NotEventCreatorException(message);
        }
        List<ParticipationRequest> requests = requestRepository.findAllParticipationRequestsByEventId(eventId);
        return requestMapper.toParticipationRequestDtoList(requests);
    }

    @Override
    public UpdateParticipationRequestStatusResult updateParticipationRequestsStatus(
            int userId, long eventId, UpdateParticipationRequestStatusDto updateRequestStatusDto) {
        Event event = checkEventExistence(eventId);
        int creatorId = event.getCreator().getId();
        int participationLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        boolean requestModeration = event.getRequestModeration();

        if (creatorId != userId) {
            String message = String.format("Невозможно выполнить операцию обновления статуса заявок, так как " +
                    "пользователь с id=%d не является автором события с id=%d!", userId, eventId);
            throw new NotEventCreatorException(message);
        }
        if (confirmedRequests == participationLimit) {
            String message = String.format("Достигнут лимит заявок на участие в событие с id=%d", eventId);
            throw new ReachLimitException(message);
        }

        List<Long> requestIds = updateRequestStatusDto.getRequestIds();
        List<ParticipationRequest> requests = requestRepository.findParticipationRequestsByIdIN(requestIds);
        checkParticipationRequestsStatus(requests); // проверяем, что полученные заявки имеют статус PENDING

        List<ParticipationRequestDto> confirmedRequestsList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequestsList = new ArrayList<>();
        ParticipationRequestStatus updatedStatus = updateRequestStatusDto.getStatus();
        int count = 0;
        for (ParticipationRequest request : requests) {
            if (participationLimit == 0 || !requestModeration) { // если лимит заявок на участие равен 0 или отключена премодерация
                request.setStatus(ParticipationRequestStatus.CONFIRMED);
                ParticipationRequestDto confirmedRequest = requestMapper.toParticipationRequestDto(request);
                confirmedRequestsList.add(confirmedRequest);
                break;
            }
            if (updatedStatus.equals(ParticipationRequestStatus.REJECTED)) {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                ParticipationRequestDto rejectedRequest = requestMapper.toParticipationRequestDto(request);
                rejectedRequestsList.add(rejectedRequest);
                break;
            }
            if (updatedStatus.equals(ParticipationRequestStatus.CONFIRMED)) {
                if (participationLimit > (confirmedRequests + count)) {
                    request.setStatus(ParticipationRequestStatus.CONFIRMED);
                    ParticipationRequestDto confirmedRequest = requestMapper.toParticipationRequestDto(request);
                    confirmedRequestsList.add(confirmedRequest);
                    count++;
                } else {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    ParticipationRequestDto rejectedRequest = requestMapper.toParticipationRequestDto(request);
                    rejectedRequestsList.add(rejectedRequest);
                }
            }
        }

        event.setConfirmedRequests(confirmedRequestsList.size());
        eventRepository.save(event); // обновляем поле confirmed_requests в БД
        return new UpdateParticipationRequestStatusResult(confirmedRequestsList, rejectedRequestsList);
    }

    @Override
    public EventFullResponseDto updateEventByAdmin(long eventId, UpdateEventDto updatedEventDto) {
        Event savedEvent = checkEventExistence(eventId);
        EventState state = savedEvent.getState();
        LocalDateTime publicationDate = getPublicationDate(savedEvent);

        if (updatedEventDto.getEventDate() != null) {
            LocalDateTime updatedEventDate = updatedEventDto.getEventDate();
            if (updatedEventDate.minusHours(1).isBefore(publicationDate)) {
                String message = String.format("Событие c id=%d должно начаться не ранее одного часа " +
                        "с момента публикации", eventId);
                throw new EventDateTimeException(message);
            }
            savedEvent.setEventDate(updatedEventDate);
        }
        if (updatedEventDto.getStateAction() != null) {
            String stateAction = updatedEventDto.getStateAction();
            StateAction st = StateAction.convertToEnum(stateAction);

            switch (st) {
                case PUBLISH_EVENT:
                    if (state.equals(EventState.PENDING)) {
                        savedEvent.setState(EventState.PUBLISHED);
                        savedEvent.setPublicationDate(publicationDate);
                    } else if (state.equals(EventState.PUBLISHED)) {
                        String message = String.format("Событие с id=%d уже опубликовано! " +
                                "Невозможно выполнить повторную публикацию", eventId);
                        throw new EventAlreadyPublishedException(message);
                    } else if (state.equals(EventState.CANCELED)) {
                        String message = String.format("Событие с id=%d отменено! " +
                                "Невозможно выполнить публикацию отмененного события", eventId);
                        throw new EventAlreadyCanceledException(message);
                    }
                    break;
                case REJECT_EVENT:
                    if (state.equals(EventState.PENDING)) {
                        savedEvent.setState(EventState.CANCELED);
                    } else if (state.equals(EventState.PUBLISHED)) {
                        String message = String.format("Событие с id=%d опубликовано! " +
                                "Невозможно отменить уже опубликованное событие", eventId);
                        throw new EventAlreadyPublishedException(message);
                    }
                    break;
            }
        }

        Event event = eventMapper.toUpdatedEvent(updatedEventDto);
        Event updatedEvent = updateEventFields(savedEvent, event);
        Event updatedInDb = eventRepository.save(updatedEvent);
        return eventMapper.toEventFullDto(updatedInDb);
    }

    @Override
    public List<EventFullResponseDto> searchEventsByAdmin(List<Integer> userIds, List<EventState> eventStates,
                                                          List<Integer> categoryIds, String rangeStart, String rangeEnd,
                                                          int from, int size) {
        Pageable pageable = Pages.getPage(from, size);
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, FORMATTER);
        } else {
            start = LocalDateTime.now().withNano(0);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, FORMATTER);
        } else {
            end = LocalDateTime.now().withNano(0).plusDays(5);
        }
        if (start.isAfter(end)) { // проверяем, что задан корректный диапазон
            String message = "Диапазон выборки задан некорректно! " +
                    "Дата конца диапазона выборки должена быть больше чем дата начала";
            throw new IllegalArgumentException(message);
        }
        List<Event> events = eventRepository.searchEventByAdmin(userIds, eventStates, categoryIds, start, end, pageable);
        return eventMapper.toEventFullDtoList(events);
    }

    @Override
    public EventFullResponseDto getEvent(long eventId, HttpServletRequest servletRequest) {
        Event event = checkEventExistence(eventId);
        EventState state = event.getState();
        if (!state.equals(EventState.PUBLISHED)) {
            String message = String.format("Событие с id=%d не найдено!", eventId);
            throw new ObjectNotFoundException(message);
        }
        saveRequestToEndpoint(servletRequest); // сохраняем запрос в сервер статистики
        int views = getViews(servletRequest); // получаем просмотры из сервера статистики
        event.setViews(views);
        eventRepository.save(event); // обновляем поле views в БД
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortResponseDto> searchEvents(String text, List<Integer> categoryIds, Boolean paid,
                                                    String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                    String sort, int from, int size, HttpServletRequest servletRequest) {
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, FORMATTER);
        } else {
            start = LocalDateTime.now().withNano(0);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, FORMATTER);
        } else {
            end = LocalDateTime.now().withNano(0).plusDays(5);
        }
        if (start.isAfter(end)) { // проверяем, что задан корректный диапазон
            String message = "Диапазон выборки задан некорректно! " +
                    "Дата конца диапазона выборки должена быть больше чем дата начала";
            throw new IllegalArgumentException(message);
        }

        Pageable pageable;
        if (sort.equals("EVENT_DATE")) {
            pageable = Pages.getSortedPage(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        } else {
            pageable = Pages.getSortedPage(from, size, Sort.by(Sort.Direction.DESC, "views"));
        }

        List<Event> events = eventRepository.searchEventsByAnyUser(text, categoryIds, paid, start, end, pageable);
        if (onlyAvailable) { // находим только те события, у которых еще не исчерпан лимит запросов на участие
            events = findOnlyAvailableEvents(events);
        }
        saveRequestToEndpoint(servletRequest); // сохраняем запрос в сервер статистики
        return eventMapper.toEventShortDtoList(events);
    }

    private User checkUserExistence(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("Пользователь с id=%d не найден!", userId);
            throw new ObjectNotFoundException(message);
        }
        return optionalUser.get();
    }

    private Event checkEventExistence(long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            String message = String.format("Событие с id=%d не найдено!", eventId);
            throw new ObjectNotFoundException(message);
        }
        return optionalEvent.get();
    }

    private LocalDateTime getPublicationDate(Event event) {
        LocalDateTime savedPublicationDate = event.getPublicationDate();
        return Objects.requireNonNullElseGet(savedPublicationDate, () -> LocalDateTime.now().withNano(0));
    }

    private Event updateEventFields(Event savedEvent, Event updatedEventData) {
        String updatedTitle = updatedEventData.getTitle();
        String updatedShortDescription = updatedEventData.getShortDescription();
        String updatedFullDescription = updatedEventData.getFullDescription();
        LocalDateTime updatedEventDate = updatedEventData.getEventDate();
        Boolean updatedPaid = updatedEventData.getPaid();
        Boolean updatedRequestModeration = updatedEventData.getRequestModeration();
        Integer updatedParticipantLimit = updatedEventData.getParticipantLimit();
        Category updatedCategory = updatedEventData.getCategory();
        Location updatedLocation = updatedEventData.getLocation();

        if (updatedTitle != null) {
            savedEvent.setTitle(updatedTitle);
        }
        if (updatedShortDescription != null) {
            savedEvent.setShortDescription(updatedShortDescription);
        }
        if (updatedFullDescription != null) {
            savedEvent.setFullDescription(updatedFullDescription);
        }
        if (updatedEventDate != null) {
            savedEvent.setEventDate(updatedEventDate);
        }
        if (updatedPaid != null) {
            savedEvent.setPaid(updatedPaid);
        }
        if (updatedRequestModeration != null) {
            savedEvent.setRequestModeration(updatedRequestModeration);
        }
        if (updatedParticipantLimit != null) {
            savedEvent.setParticipantLimit(updatedParticipantLimit);
        }
        if (updatedCategory != null) {
            savedEvent.setCategory(updatedCategory);
        }
        if (updatedLocation != null) {
            savedEvent.setLocation(updatedLocation);
        }
        return savedEvent;
    }

    private void checkParticipationRequestsStatus(List<ParticipationRequest> requests) {
        for (ParticipationRequest request : requests) {
            ParticipationRequestStatus status = request.getStatus();
            if (!status.equals(ParticipationRequestStatus.PENDING)) {
                String message = String.format("Заявка с id=%d имеет статус '%s'! Подтвердить или отклонить заявку " +
                        "можно только со статусом '%s'", request.getId(), status, ParticipationRequestStatus.PENDING);
                throw new ConflictException(message);
            }
        }
    }

    private void saveRequestToEndpoint(HttpServletRequest servletRequest) {
        String uri = servletRequest.getRequestURI();
        String ipAddress = servletRequest.getRemoteAddr(); // получаем IP-адрес клиента, который делает запрос к эндпоинту
        String applicationName = "ewm-main-service";
        LocalDateTime timestamp = LocalDateTime.now().withNano(0);

        StatDto statDto = new StatDto(applicationName, uri, ipAddress, timestamp);
        statisticClient.saveInformation(statDto);
    }

    private List<Event> findOnlyAvailableEvents(List<Event> events) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            int participationLimit = event.getParticipantLimit();
            int confirmedRequests = event.getConfirmedRequests();
            if (confirmedRequests < participationLimit) {
                result.add(event);
            }
        }
        return result;
    }

    private int getViews(HttpServletRequest servletRequest) {
        String uri = servletRequest.getRequestURI();
        String start = LocalDateTime.now().minusMonths(1).withNano(0).format(FORMATTER);
        String end = LocalDateTime.now().plusMonths(1).withNano(0).format(FORMATTER);

        List<ViewStatDto> result = statisticClient.getStatistic(start, end, List.of(uri), true);
        Long views = result.get(0).getHits();
        return views.intValue();
    }
}
