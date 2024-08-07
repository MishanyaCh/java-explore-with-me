package ru.practicum.compilation.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.model.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // заголовок подборки

    @Column(name = "pinned")
    private Boolean isPinned; // закреплена ли подборка на главной странице

    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private List<Event> events;

    public Compilation() {
    }

    public Compilation(Long idArg, String titleArg, Boolean isPinnedArg, List<Event> eventsArg) {
        id = idArg;
        title = titleArg;
        isPinned = isPinnedArg;
        events = eventsArg;
    }
}
