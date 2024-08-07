package ru.practicum.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ViewStatDto;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate restTemplate;

    public BaseClient(RestTemplate restTemplateArg) {
        restTemplate = restTemplateArg;
    }

    protected <T, V> ResponseEntity<V> post(String path, T body, Class<V> responseType) {
        return makeAndSendRequestForSaveStat(HttpMethod.POST, path, null, body, responseType);
    }

    protected ResponseEntity<List<ViewStatDto>> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequestForGetStat(HttpMethod.GET, path, parameters);
    }

    private <T, V> ResponseEntity<V> makeAndSendRequestForSaveStat(HttpMethod method, String path,
                                                                   @Nullable Map<String, Object> parameters,
                                                                   @Nullable T body, Class<V> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<V> serverResponse;

        try {
            if (parameters != null) {
                serverResponse = restTemplate.exchange(path, method, requestEntity, responseType, parameters);

            } else {
                serverResponse = restTemplate.exchange(path, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException exc) {
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(exc.getStatusCode());
            ResponseEntity<String> responseEntity = bodyBuilder.body(exc.getResponseBodyAsString());
            throw new RuntimeException(responseEntity.getBody());
        }
        return prepareClientResponse(serverResponse);
    }

    private ResponseEntity<List<ViewStatDto>> makeAndSendRequestForGetStat(HttpMethod method, String path,
                                                                           @Nullable Map<String, Object> parameters) {
        HttpEntity<List<ViewStatDto>> requestEntity = new HttpEntity<>(null, defaultHeaders());
        ResponseEntity<List<ViewStatDto>> serverResponse;

        try {
            if (parameters != null) {
                serverResponse = restTemplate.exchange(path, method, requestEntity,
                        new ParameterizedTypeReference<List<ViewStatDto>>() {}, parameters);
            } else {
                serverResponse = restTemplate.exchange(path, method, requestEntity,
                        new ParameterizedTypeReference<List<ViewStatDto>>() {});
            }
        } catch (HttpStatusCodeException exc) {
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(exc.getStatusCode());
            ResponseEntity<String> responseEntity = bodyBuilder.body(exc.getResponseBodyAsString());
            throw new RuntimeException(responseEntity.getBody());
        }
        return prepareClientResponse(serverResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <T> ResponseEntity<T> prepareClientResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
