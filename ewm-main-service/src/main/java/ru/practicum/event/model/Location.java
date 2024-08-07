package ru.practicum.event.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float latitude; // широта
    private float longitude; // долгота

    public Location() {
    }

    public Location(Integer idArg, float latitudeArg, float longitudeArg) {
        id = idArg;
        latitude = latitudeArg;
        longitude = longitudeArg;
    }
}
