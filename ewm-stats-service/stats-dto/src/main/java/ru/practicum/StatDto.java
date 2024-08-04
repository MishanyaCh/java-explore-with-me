package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class StatDto {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotBlank(message = "Поле 'app' должно содержать хотя бы один непробельный символ!")
    private String app;

    @NotNull(message = "Поле 'uri' должно присутствовать!")
    private String uri;

    @NotNull(message = "Поле 'ip' должно присутствовать!")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime timestamp;

    public StatDto(String appArg, String uriArg, String ipArg, LocalDateTime timestampArg) {
        app = appArg;
        uri = uriArg;
        ip = ipArg;
        timestamp = timestampArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String result = "StatInputDto{" + "applicationName='" + app + '\'' + ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'';
        if (timestamp == null) {
            result = result + ", timestamp=null";
        } else {
            result = result + ", timestamp=" + timestamp.format(formatter);
        }
        return result + "}";
    }
}
