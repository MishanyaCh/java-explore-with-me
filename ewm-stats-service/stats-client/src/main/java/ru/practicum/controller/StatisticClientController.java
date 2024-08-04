package ru.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;
import ru.practicum.client.StatisticClient;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StatisticClientController {
    private static final Logger log = LoggerFactory.getLogger(StatisticClientController.class);
    private final StatisticClient statisticClient;

    @Autowired
    public StatisticClientController(StatisticClient statisticClientArg) {
        statisticClient = statisticClientArg;

    }

    @PostMapping(path = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public StatDto saveInformation(@RequestBody @Valid StatDto statDto) {
        log.info("Пришел POST /hit запрос с телом: {}", statDto);
        final StatDto savedInfo = statisticClient.saveInformation(statDto);
        log.info("На POST /hit запрос отправлен ответ с телом: {}", savedInfo);
        return savedInfo;
    }

    @GetMapping(path = "/stats")
    public List<ViewStatDto> getStatistic(@RequestParam String start, @RequestParam String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam Boolean unique) {
        log.info("Пришел GET /stats?start={}&end={}&uris={}&unique={} запрос", start, end, uris, unique);
        final List<ViewStatDto> result = statisticClient.getStatistic(start, end, uris, unique);
        log.info("На GET /stats?start={}&end={}&uris={}&unique={} запрос отправлен ответ: {}",
                start, end, uris, unique, result);
        return result;
    }
}
