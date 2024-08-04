package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;
import ru.practicum.mapper.StatisticMapper;
import ru.practicum.model.StatRecord;
import ru.practicum.storage.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final StatisticMapper statisticMapper;
    private final StatisticRepository statisticRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatisticServiceImpl(StatisticMapper statisticMapperArg, StatisticRepository statisticRepositoryArg) {
        statisticMapper = statisticMapperArg;
        statisticRepository = statisticRepositoryArg;
    }

    @Override
    public StatDto saveInformation(StatDto statDto) {
        StatRecord statRecord = statisticMapper.toStatRecord(statDto);
        StatRecord createdStatRecord = statisticRepository.save(statRecord); // сохраняем данные в таблицу statistic
        return statisticMapper.toStatDto(createdStatRecord);
    }

    @Override
    public List<ViewStatDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        List<ViewStatDto> result;

        if (unique) { // если unique = true, то в подсчете статистики участвуют только неповторяющиеся IP-адреса пользователей
            if (uris.isEmpty()) {
                result = statisticRepository.getStatisticForUniqueIpByTimeInterval(startDate, endDate);
            } else {
                result = statisticRepository.getStatisticForUniqueIpByTimeIntervalAndUrisList(startDate, endDate, uris);
            }
        } else {
            if (uris.isEmpty()) {
                result = statisticRepository.getStatisticByTimeInterval(startDate, endDate);
            } else {
                result = statisticRepository.getStatisticByTimeIntervalAndUrisList(startDate, endDate, uris);
            }
        }
        return result;
    }
}
