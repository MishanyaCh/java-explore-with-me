package ru.practicum.mapper;

import ru.practicum.StatDto;
import ru.practicum.model.StatRecord;

public interface StatisticMapper {
    StatRecord toStatRecord(StatDto inputDto);

    StatDto toStatDto(StatRecord statRecord);
}
