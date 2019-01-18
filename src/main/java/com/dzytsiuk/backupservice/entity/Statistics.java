package com.dzytsiuk.backupservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Statistics {
    @Id
    private String id;
    private LocalDateTime dateCreated;
    private long xlsxCount;
    private long pdfCount;
    private long movieTypeRequestCount;
    private long userTypeRequestCount;
    private StatisticsReport reportWithMaxProcessingTime;
    private StatisticsReport reportWithMinProcessingTime;
    private double averageProcessingTime;
    private List<UserStatistics> userStatistics;

}
