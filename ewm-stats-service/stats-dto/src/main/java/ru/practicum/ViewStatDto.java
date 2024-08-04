package ru.practicum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStatDto {
    private String app;
    private String uri;
    private Long hits;

    public ViewStatDto(String appArg, String uriArg, Long hitsArg) {
        app = appArg;
        uri = uriArg;
        hits = hitsArg;
    }

    @Override
    public String toString() {
        return "ViewStatDto{" + "applicationName='" + app + '\'' + ", uri='" + uri + '\'' + ", hits=" + hits + "}";
    }
}
