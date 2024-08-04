package ru.practicum.participationRequest.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.enums.ParticipationRequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ParticipationRequestStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requester_id")
    private User requester;

    public ParticipationRequest() {
    }

    public ParticipationRequest(Long idArg, ParticipationRequestStatus statusArg, LocalDateTime creationDateArg,
                                Event eventArg, User requesterArg) {
        id = idArg;
        status = statusArg;
        creationDate = creationDateArg;
        event = eventArg;
        requester = requesterArg;
    }
}
