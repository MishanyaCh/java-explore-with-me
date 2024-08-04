package ru.practicum.client;

import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;

import java.util.ArrayList;
import java.util.List;

public interface StatisticClient {
    StatDto saveInformation(StatDto statDto);

    List<ViewStatDto> getStatistic(String start, String end, List<String> uris, Boolean unique);
}
