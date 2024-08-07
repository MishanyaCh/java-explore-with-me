package ru.practicum.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.category.model.Category;
import ru.practicum.enums.EventState;
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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // заголовок события

    @Column(name = "short_description")
    private String shortDescription; // краткое описание

    @Column(name = "full_description")
    private String fullDescription; // полное описание

    @Column(name = "event_date")
    private LocalDateTime eventDate; // дата и время начала события

    @Column(name = "creation_date")
    private LocalDateTime creationDate; // дата и время создание

    @Enumerated(value = EnumType.STRING)
    private EventState state;
    private Boolean paid; // нужно ли оплачивать участие в событии

    @Column(name = "request_moderation")
    private Boolean requestModeration; // нужна ли пре-модерация заявок. Если true, то все заявки на участие в событии
                                       // ожидают подтверждение создателем события. Если false -> автоматическое подтверждение
    @Column(name = "participant_limit")
    private Integer participantLimit; // ограничение на кол-во участников. Если значение = 0, то ограничения нет.

    @Column(name = "publication_date")
    private LocalDateTime publicationDate; // дата и время публикации события

    @Column(name = "confirmed_requests")
    private int confirmedRequests; // количество одобренных заявок на участие в событии, т.е кол-во участников
    private int views; // кол-во просмотров события

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category; // категория события

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location; // координаты местопроведения события

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator; // автор события

    public Event() {
    }

    public Event(Long idArg, String titleArg, String shortDescriptionArg, String fullDescriptionArg,
                 LocalDateTime eventDateArg, LocalDateTime creationDateArg, EventState stateArg, Boolean paidArg,
                 Boolean requestModerationArg, Integer participantLimitArg, LocalDateTime publicationDateArg,
                 int confirmedRequestsArg, int viewsArg, Category categoryArg, Location locationArg, User creatorArg) {
        id = idArg;
        title = titleArg;
        shortDescription = shortDescriptionArg;
        fullDescription = fullDescriptionArg;
        eventDate = eventDateArg;
        creationDate = creationDateArg;
        state = stateArg;
        paid = paidArg;
        requestModeration = requestModerationArg;
        participantLimit = participantLimitArg;
        publicationDate = publicationDateArg;
        confirmedRequests = confirmedRequestsArg;
        views = viewsArg;
        category = categoryArg;
        location = locationArg;
        creator = creatorArg;
    }
}
