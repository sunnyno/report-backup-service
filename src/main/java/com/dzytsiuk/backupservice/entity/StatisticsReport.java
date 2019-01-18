package com.dzytsiuk.backupservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsReport {
    private List<ReportRequest> reportRequests;
    private long parameter;
}
