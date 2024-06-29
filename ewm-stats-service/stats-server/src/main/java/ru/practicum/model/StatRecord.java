package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Getter
@Setter
public class StatRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;
    private String uri;

    @Column(name = "IP_address")
    private String ip;

    @Column(name = "date_of_service_request")
    private LocalDateTime timestamp;

    public StatRecord() {
    }

    public StatRecord(Long idArg, String serviceNameArg, String uriArg, String ipArg, LocalDateTime timestampArg) {
        id = idArg;
        serviceName = serviceNameArg;
        uri = uriArg;
        ip = ipArg;
        timestamp = timestampArg;
    }
}
