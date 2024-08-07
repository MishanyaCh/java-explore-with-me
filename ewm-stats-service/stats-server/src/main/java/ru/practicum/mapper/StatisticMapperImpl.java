package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.StatDto;
import ru.practicum.model.StatRecord;

import java.time.LocalDateTime;

@Component
public class StatisticMapperImpl implements StatisticMapper {
    @Override
    public StatRecord toStatRecord(StatDto inputDto) {
        String serviceName = inputDto.getApp();
        String uri = inputDto.getUri();
        String ip = inputDto.getIp();
        LocalDateTime timestamp = inputDto.getTimestamp();
        return new StatRecord(null, serviceName, uri, ip, timestamp);
    }

    @Override
    public StatDto toStatDto(StatRecord statRecord) {
        String serviceName = statRecord.getServiceName();
        String uri = statRecord.getUri();
        String ip = statRecord.getIp();
        LocalDateTime timestamp = statRecord.getTimestamp();
        return new StatDto(serviceName, uri, ip, timestamp);
    }
}
