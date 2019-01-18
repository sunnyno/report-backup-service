package com.dzytsiuk.backupservice.service;

import com.dzytsiuk.backupservice.entity.ReportRequest;

import java.util.List;

public interface StatisticsService {
    void generateStatistics(List<ReportRequest> reportRequests);
}
