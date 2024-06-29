package ru.practicum.service;

import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;

import java.util.List;

public interface StatisticService {
    StatDto saveInformation(StatDto statDto);

    List<ViewStatDto> getStatistic(String start, String end, List<String> uris, Boolean unique);
}
