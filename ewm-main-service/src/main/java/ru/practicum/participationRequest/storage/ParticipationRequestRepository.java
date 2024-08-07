package ru.practicum.participationRequest.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.participationRequest.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsParticipationRequestByRequesterIdAndEventId(int requesterId, long eventId);

    @Query(value = "SELECT * " +
            "FROM (SELECT * FROM participation_requests WHERE requester_id = :requesterId) AS req " +
            "INNER JOIN events AS ev ON req.event_id = ev.id " +
            "INNER JOIN users AS u ON req.requester_id = u.id ", nativeQuery = true)
    List<ParticipationRequest> findAllParticipationRequestsByRequesterId(int requesterId);

    @Query(value = "SELECT * " +
            "FROM (SELECT * FROM participation_requests WHERE event_id = :eventId) AS req " +
            "INNER JOIN events AS ev ON req.event_id = ev.id " +
            "INNER JOIN users AS u ON req.requester_id = u.id ", nativeQuery = true)
    List<ParticipationRequest> findAllParticipationRequestsByEventId(long eventId);

    @Query(value = "SELECT * " +
            "FROM (SELECT * FROM participation_requests WHERE id IN :requestIds) AS req " +
            "INNER JOIN events AS ev ON req.event_id = ev.id " +
            "INNER JOIN users AS u ON req.requester_id = u.id ", nativeQuery = true)
    List<ParticipationRequest> findParticipationRequestsByIdIN(List<Long> requestIds);
}
