package ru.practicum.participationRequest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventState;
import ru.practicum.enums.ParticipationRequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.EventNotPublishedException;
import ru.practicum.exception.NotParticipationRequestCreatorException;
import ru.practicum.exception.ObjectAlreadyExistException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ReachLimitException;
import ru.practicum.exception.EventCreatorException;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.storage.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;

    @Autowired
    public ParticipationRequestServiceImpl(UserRepository userRepositoryArg, EventRepository eventRepositoryArg,
                                           ParticipationRequestRepository requestRepositoryArg,
                                           ParticipationRequestMapper requestMapperArg) {
        userRepository = userRepositoryArg;
        eventRepository = eventRepositoryArg;
        requestRepository = requestRepositoryArg;
        requestMapper = requestMapperArg;
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(int userId, long eventId) {
        User requester = checkUserExistence(userId);
        Event event = checkEventExistence(eventId);
        checkParticipationRequestDuplicate(userId, eventId);
        checkEventData(event, userId);

        ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;
        int participationLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        if (!event.getRequestModeration() || participationLimit == 0) { // если requestModeration = false, то заявка подтверждается автоматически
            status = ParticipationRequestStatus.CONFIRMED;
            event.setConfirmedRequests(confirmedRequests + 1); // увеличиваем кол-во подтвержденных запросов на участие
            eventRepository.save(event); // обновляем поле confirmedRequests в БД
        }

        ParticipationRequest request = new ParticipationRequest(null, status, LocalDateTime.now(), event, requester);
        ParticipationRequest createdRequest = requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(createdRequest);
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(int userId, long requestId) {
        checkUserExistence(userId);
        Optional<ParticipationRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            String message = String.format("Заявка с id=%d на участие в мероприятии не найдена!", requestId);
            throw new ObjectNotFoundException(message);
        }
        ParticipationRequest request = optionalRequest.get();

        if (request.getRequester().getId() != userId) {
            String message = String.format("Невозможно отменить заявку с id=%d, так как пользователь с id=%d " +
                    "не является автором данной заявки.", requestId, userId);
            throw new NotParticipationRequestCreatorException(message);
        }

        request.setStatus(ParticipationRequestStatus.CANCELED);
        ParticipationRequest updatedRequest = requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(updatedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsList(int userId) {
        checkUserExistence(userId);
        List<ParticipationRequest> requests = requestRepository.findAllParticipationRequestsByRequesterId(userId);
        return requestMapper.toParticipationRequestDtoList(requests);
    }

    private User checkUserExistence(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("Пользователь с id=%d не найден! " +
                    "Невозможно создать заявку на участие в событии.", userId);
            throw new ObjectNotFoundException(message);
        }
        return optionalUser.get();
    }

    private Event checkEventExistence(long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            String message = String.format("Событие с id=%d не найдено! " +
                    "Невозможно создать заявку на участие в событии.", eventId);
            throw new ObjectNotFoundException(message);
        }
        return optionalEvent.get();
    }

    private void checkParticipationRequestDuplicate(int userId, long eventId) {
        boolean isDuplicate = requestRepository.existsParticipationRequestByRequesterIdAndEventId(userId, eventId);
        if (isDuplicate) {
            String message = String.format("Пользователь с id=%d уже подал заявку на участие в событие с id=%d.",
                    userId, eventId);
            throw new ObjectAlreadyExistException(message);
        }
    }

    private void checkEventData(Event event, int userId) {
        EventState state = event.getState();
        int eventCreatorId = event.getCreator().getId();
        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();

        if (!state.equals(EventState.PUBLISHED)) {
            String message = String.format("Событие с id=%d неопубликовано! " +
                    "Невозможно создать заявку на участие в неопубликованном событии.", event.getId());
            throw new EventNotPublishedException(message);
        }
        if (eventCreatorId == userId) {
            String message = "Автор события не может добавлять запрос на участие в своем событии!";
            throw new EventCreatorException(message);
        }
        if (participantLimit > 0 & confirmedRequests == participantLimit) {
            String message = String.format("Для события с id=%d достигнут лимит участников!", event.getId());
            throw new ReachLimitException(message);
        }
    }
}
