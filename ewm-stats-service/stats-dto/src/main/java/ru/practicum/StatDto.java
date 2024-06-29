package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(value = "app")
    @NotBlank(message = "Имя сервиса должно содержать хотя бы один непробельный символ!")
    private String serviceName;

    @NotNull(message = "Поле uri должно присутствовать!")
    private String uri;

    @NotNull(message = "Поле ip должно присутствовать!")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime timestamp;

    public StatDto(String serviceNameArg, String uriArg, String ipArg, LocalDateTime timestampArg) {
        serviceName = serviceNameArg;
        uri = uriArg;
        ip = ipArg;
        timestamp = timestampArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "StatInputDto{" + "serviceName='" + serviceName + '\'' + ", uri='" + uri + '\'' + ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp.format(formatter) + "}";
    }
}
