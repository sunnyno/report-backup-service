package com.dzytsiuk.backupservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequest {
    @Id
    private String id;
    private ReportType reportType;
    private ReportParameter reportParameter;
    private ReportStatus reportStatus;
    private ReportFormat reportFormat;
    private String ftpUrl;
    private LocalDateTime dateRequested;
    private LocalDateTime dateProcessed;
    private Long processingTime;//seconds
    private User user;
}
