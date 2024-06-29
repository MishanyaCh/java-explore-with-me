package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import ru.practicum.StatDto;

import java.util.List;

public interface StatisticClient {
    ResponseEntity<Object> saveInformation(StatDto statDto);

    ResponseEntity<Object> getStatistic(String start, String end, List<String> uris, Boolean unique);
}
