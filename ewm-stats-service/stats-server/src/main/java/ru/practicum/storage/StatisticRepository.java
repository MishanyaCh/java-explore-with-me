package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatDto;
import ru.practicum.model.StatRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<StatRecord, Integer> {
    @Query(value = "SELECT new ru.practicum.ViewStatDto(st.serviceName, st.uri, COUNT(st.ip)) " +
            "FROM StatRecord AS st " +
            "WHERE (st.timestamp BETWEEN :start AND :end) AND st.uri IN :uris " +
            "GROUP BY st.serviceName, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<ViewStatDto> getStatisticByTimeIntervalAndUrisList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.ViewStatDto(st.serviceName, st.uri, COUNT(st.ip)) " +
            "FROM StatRecord AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.serviceName, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<ViewStatDto> getStatisticByTimeInterval(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStatDto(st.serviceName, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM StatRecord AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.serviceName, st.uri " +
            "ORDER BY COUNT(DISTINCT st.ip) DESC")
    List<ViewStatDto> getStatisticForUniqueIpByTimeInterval(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ViewStatDto(st.serviceName, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM StatRecord AS st " +
            "WHERE (st.timestamp BETWEEN :start AND :end) AND st.uri IN :uris " +
            "GROUP BY st.serviceName, st.uri " +
            "ORDER BY COUNT(DISTINCT st.ip) DESC")
    List<ViewStatDto> getStatisticForUniqueIpByTimeIntervalAndUrisList(LocalDateTime start, LocalDateTime end,
                                                                       List<String> uris);
}
