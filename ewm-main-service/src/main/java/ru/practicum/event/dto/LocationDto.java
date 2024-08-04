package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    @JsonProperty(value = "lat")
    private float latitude;

    @JsonProperty(value = "lon")
    private float longitude;

    public LocationDto(float latitudeArg, float longitudeArg) {
        latitude = latitudeArg;
        longitude = longitudeArg;
    }

    @Override
    public String toString() {
        return "LocationDto{" + "latitude=" + latitude + ", longitude=" + longitude + "}";
    }
}
