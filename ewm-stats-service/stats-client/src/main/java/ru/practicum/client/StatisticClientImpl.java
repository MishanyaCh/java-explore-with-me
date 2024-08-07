package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatDto;
import ru.practicum.ViewStatDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticClientImpl extends BaseClient implements StatisticClient {
    @Autowired
    public StatisticClientImpl(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public StatDto saveInformation(StatDto statDto) {
        ResponseEntity<StatDto> response = post("/hit", statDto, StatDto.class);
        return response.getBody();
    }

    public List<ViewStatDto> getStatistic(String start, String end, List<String> urisList, Boolean unique) {
        String[] uris = convertUrisListToArray(urisList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", uris);
        parameters.put("unique", unique);

        ResponseEntity<List<ViewStatDto>> response =
                get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        return response.getBody();
    }

    private String[] convertUrisListToArray(List<String> urisList) {
        String[] uris = new String[urisList.size()];
        for (int i = 0; i < urisList.size(); i++) {
            uris[i] = urisList.get(i);
        }
        return uris;
    }
}
