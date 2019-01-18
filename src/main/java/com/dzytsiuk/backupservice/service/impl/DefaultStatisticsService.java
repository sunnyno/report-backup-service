package com.dzytsiuk.backupservice.service.impl;

import com.dzytsiuk.backupservice.entity.*;
import com.dzytsiuk.backupservice.repository.StatisticsRepository;
import com.dzytsiuk.backupservice.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultStatisticsService implements StatisticsService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final StatisticsRepository statisticsRepository;

    public DefaultStatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public void generateStatistics(List<ReportRequest> reportRequests) {
        Statistics statistics = new Statistics();
        String id = UUID.randomUUID().toString();
        statistics.setId(id);
        statistics.setDateCreated(LocalDateTime.now());
        log.info("Started statistics {} processing", id);
        enrichWithReportFormatCounts(reportRequests, statistics);
        enrichWithReportTypeCounts(reportRequests, statistics);
        enrichWithReportTimeStat(reportRequests, statistics);

        Map<User, List<ReportRequest>> userRequestMap = reportRequests.stream().collect(Collectors.groupingBy(ReportRequest::getUser));
        List<UserStatistics> userStatisticsList = new ArrayList<>();
        for (Map.Entry<User, List<ReportRequest>> userRequests : userRequestMap.entrySet()) {
            UserStatistics userStatistics = new UserStatistics();
            User user = userRequests.getKey();
            userStatistics.setUser(user);
            List<ReportRequest> requests = userRequests.getValue();
            enrichWithUserReportStat(userStatistics, requests);
            userStatisticsList.add(userStatistics);
        }
        statisticsRepository.save(statistics);
        log.info("Statistics {} save to DB", statistics);
    }

    private void enrichWithUserReportStat(UserStatistics userStatistics, List<ReportRequest> requests) {
        int userRequestCount = requests.size();
        userStatistics.setUserRequestCount(userRequestCount);
        long userRequestMovieTypeCount = getMovieReportCount(requests.stream());
        userStatistics.setUserRequestMovieTypeCount(userRequestMovieTypeCount);
        long userRequestUserTypeCount = getUserReportCount(requests.stream());
        userStatistics.setUserRequestUserTypeCount(userRequestUserTypeCount);
        long userRequestXlsxCount = getReportFormatCount(requests.stream(), ReportFormat.XLSX);
        userStatistics.setUserRequestXlsxCount(userRequestXlsxCount);
        long userRequestPdfCount = getReportFormatCount(requests.stream(), ReportFormat.PDF);
        userStatistics.setUserRequestPdfCount(userRequestPdfCount);
    }

    private void enrichWithReportTimeStat(List<ReportRequest> reportRequests, Statistics statistics) {
        LongSummaryStatistics requestTimeStatistics = reportRequests.stream().mapToLong(ReportRequest::getProcessingTime).summaryStatistics();
        long maxReportProcessingTime = requestTimeStatistics.getMax();
        List<ReportRequest> reportsWithMaxProcessingTime = reportRequests.stream().filter(request -> request.getProcessingTime() == maxReportProcessingTime).collect(Collectors.toList());
        statistics.setReportWithMaxProcessingTime(new StatisticsReport(reportsWithMaxProcessingTime, maxReportProcessingTime));
        long minReportProcessingTime = requestTimeStatistics.getMin();
        List<ReportRequest> reportsWithMinProcessingTime = reportRequests.stream().filter(request -> request.getProcessingTime() == minReportProcessingTime).collect(Collectors.toList());
        statistics.setReportWithMinProcessingTime(new StatisticsReport(reportsWithMinProcessingTime, minReportProcessingTime));

        double averageProcessingTime = requestTimeStatistics.getAverage();
        statistics.setAverageProcessingTime(averageProcessingTime);
    }

    private void enrichWithReportTypeCounts(List<ReportRequest> reportRequests, Statistics statistics) {
        long movieTypeRequestCount = getMovieReportCount(reportRequests.stream());
        statistics.setMovieTypeRequestCount(movieTypeRequestCount);
        long userTypeRequestCount = getUserReportCount(reportRequests.stream());
        statistics.setUserTypeRequestCount(userTypeRequestCount);
    }

    private void enrichWithReportFormatCounts(List<ReportRequest> reportRequests, Statistics statistics) {
        long xlsxCount = getReportFormatCount(reportRequests.stream(), ReportFormat.XLSX);
        statistics.setXlsxCount(xlsxCount);
        long pdfCount = getReportFormatCount(reportRequests.stream(), ReportFormat.PDF);
        statistics.setPdfCount(pdfCount);
    }

    private long getUserReportCount(Stream<ReportRequest> stream) {
        return stream.filter(reportRequest -> reportRequest.getReportType() == ReportType.TOP_ACTIVE_USERS).count();
    }

    private long getMovieReportCount(Stream<ReportRequest> reportRequestStream) {
        return reportRequestStream.filter(this::isMovieRequest).count();
    }

    private long getReportFormatCount(Stream<ReportRequest> reportRequestStream, ReportFormat format) {
        return reportRequestStream.filter(request -> request.getReportFormat() == format).count();
    }

    private boolean isMovieRequest(ReportRequest request) {
        ReportType reportType = request.getReportType();
        return reportType == ReportType.ADDED_DURING_PERIOD || reportType == ReportType.ALL_MOVIES;
    }
}
