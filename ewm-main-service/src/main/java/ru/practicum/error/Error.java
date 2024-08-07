package ru.practicum.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Error {
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private String status;
    private String reason;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIMEFORMAT)
    private LocalDateTime timestamp;

    public Error(String statusArg, String reasonArg, String messageArg, LocalDateTime timestampArg) {
        status = statusArg;
        reason = reasonArg;
        message = messageArg;
        timestamp = timestampArg;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        return "Error{" + "status='" + status + '\'' + ", reason='" + reason + '\'' + ", message='" + message + '\'' +
                ", timestamp=" + timestamp.format(formatter) + "}";
    }
}
