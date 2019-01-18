package com.dzytsiuk.backupservice.service.impl;

import com.dzytsiuk.backupservice.entity.ReportRequest;
import com.dzytsiuk.backupservice.service.ReportProcessingService;
import com.dzytsiuk.backupservice.service.ReportService;
import com.dzytsiuk.backupservice.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DefaultReportProcessingService implements ReportProcessingService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Queue<ReportRequest> reportRequests = new ConcurrentLinkedQueue<>();

    private final ReportService reportService;
    private final StatisticsService statisticsService;

    public DefaultReportProcessingService(ReportService reportService, StatisticsService statisticsService) {
        this.reportService = reportService;
        this.statisticsService = statisticsService;
    }

    @JmsListener(destination = "generated", containerFactory = "topicListenerFactory")
    private void getGeneratedReport(ReportRequest reportRequest) {
        reportRequest.setDateProcessed(LocalDateTime.now());
        reportRequest.setProcessingTime(ChronoUnit.SECONDS.between(reportRequest.getDateRequested(), reportRequest.getDateProcessed()));
        log.info("Report {} received", reportRequest);
        reportRequests.add(reportRequest);
        reportService.save(reportRequest);
    }

    @Scheduled(fixedRateString = "${statistics.processing.rate}")
    private void generateStatistics() {
        if (reportRequests.size() == 0) {
            return;
        }
        ArrayList<ReportRequest> reportRequestsCopy = new ArrayList<>(reportRequests);
        log.info("Start calculating statistics");

        statisticsService.generateStatistics(reportRequestsCopy);

        log.info("Finish calculating statistics");
        for (ReportRequest reportRequest : reportRequestsCopy) {
            reportRequests.remove(reportRequest);
        }
    }

}
