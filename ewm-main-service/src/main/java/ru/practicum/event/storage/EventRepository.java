package ru.practicum.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.EventState;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    long countEventsByCategoryId(int categoryId);

    List<Event> findAllEventsByCreatorId(int creatorId, Pageable pageable);

    @Query(value = "SELECT ev FROM Event ev " +
            "WHERE (:userIds IS NULL OR ev.creator.id IN :userIds) " +
            "AND (:eventStates IS NULL OR ev.state IN :eventStates) " +
            "AND (:categoryIds IS NULL OR ev.category.id IN :categoryIds) " +
            "AND (ev.eventDate > :rangeStart AND ev.eventDate < :rangeEnd) ")
    List<Event> searchEventByAdmin(List<Integer> userIds, List<EventState> eventStates, List<Integer> categoryIds,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT ev FROM Event ev " +
            "WHERE ev.state = 'PUBLISHED' " +
            "AND (:text IS NULL OR (LOWER(ev.shortDescription) LIKE CONCAT('%', lower(:text), '%') " +
                                    "OR LOWER(ev.fullDescription) LIKE CONCAT('%', lower(:text), '%'))) " +
            "AND (:categoryIds IS NULL OR ev.category.id IN :categoryIds) " +
            "AND (:paid IS NULL OR ev.paid = :paid) " +
            "AND (ev.eventDate > :rangeStart AND ev.eventDate < :rangeEnd)")
    List<Event> searchEventsByAnyUser(String text, List<Integer> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Pageable pageable);
}
