package ru.practicum;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStatDto {
    @JsonProperty("app")
    private String serviceName;
    private String uri;
    private Long hits;

    public ViewStatDto(String serviceNameArg, String uriArg, Long hitsArg) {
        serviceName = serviceNameArg;
        uri = uriArg;
        hits = hitsArg;
    }

    @Override
    public String toString() {
        return "ViewStatDto{" + "serviceName='" + serviceName + '\'' + ", uri='" + uri + '\'' + ", hits=" + hits + "}";
    }
}
