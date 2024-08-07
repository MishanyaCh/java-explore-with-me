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
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StatisticController {
    private static final Logger log = LoggerFactory.getLogger(StatisticController.class);
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticServiceArg) {
        statisticService = statisticServiceArg;
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public StatDto saveStatisticInformation(@RequestBody @Valid StatDto statDto) {
        log.info("Пришел POST /hit запрос с телом: {}", statDto);
        final StatDto savedInfo = statisticService.saveInformation(statDto);
        log.info("На POST /hit запрос отправлен ответ c телом: {}", savedInfo);
        return savedInfo;
    }

    @GetMapping(path = "/stats")
    public List<ViewStatDto> getStatistic(@RequestParam String start, @RequestParam String end,
                                          @RequestParam(required = false, defaultValue = "") List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Пришел GET /stats?start={}&end={}&uris={}&unique={} запрос", start, end, uris, unique);
        final List<ViewStatDto> result = statisticService.getStatistic(start, end, uris, unique);
        log.info("На GET /stats?start={}&end={}&uris={}&unique={} запрос отправлен ответ с размером тела: {}",
                start, end, uris, unique, result.size());
        return result;
    }
}
