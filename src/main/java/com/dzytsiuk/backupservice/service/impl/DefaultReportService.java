package com.dzytsiuk.backupservice.service.impl;

import com.dzytsiuk.backupservice.entity.ReportFileMapping;
import com.dzytsiuk.backupservice.entity.ReportRequest;
import com.dzytsiuk.backupservice.repository.ReportFileMappingRepository;
import com.dzytsiuk.backupservice.repository.ReportRepository;
import com.dzytsiuk.backupservice.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class DefaultReportService implements ReportService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ReportRepository reportRepository;
    private final GridFsTemplate gridFsTemplate;
    private final ReportFileMappingRepository reportFileMappingRepository;
    @Value("${ftp.password}")
    private String ftpPassword;

    public DefaultReportService(ReportRepository reportRepository, GridFsTemplate gridFsTemplate, ReportFileMappingRepository reportFileMappingRepository) {
        this.reportRepository = reportRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.reportFileMappingRepository = reportFileMappingRepository;
    }

    @Override
    public void save(ReportRequest reportRequest) {
        String requestId = reportRequest.getId();
        log.info("Saving report request {} to DB", requestId);
        reportRepository.save(reportRequest);
        saveFile(reportRequest);
    }

    private void saveFile(ReportRequest reportRequest) {
        String requestId = reportRequest.getId();
        log.info("Saving report {} file to DB", requestId);
        String ftpUrl = reportRequest.getFtpUrl();
        StringBuilder sb = new StringBuilder(ftpUrl);
        sb.insert(ftpUrl.indexOf('@'), ":" + ftpPassword);
        try (InputStream in = new URL(sb.toString()).openStream()) {
            String fileName = getFileName(reportRequest);
            String fileId = gridFsTemplate.store(in, fileName).toString();
            reportFileMappingRepository.save(ReportFileMapping.builder()
                    .fileId(fileId)
                    .fileName(fileName)
                    .requestId(requestId)
                    .build());
            log.info("File {} saved to Db. File id - {}, request id - ", fileName, fileId, requestId);
        } catch (IOException e) {
            throw new RuntimeException("Error saving report file " + requestId + " to DB", e);
        }
    }

    private String getFileName(ReportRequest reportRequest) {
        return reportRequest.getReportType().getName() + "_" + reportRequest.getId() + "." + reportRequest.getReportFormat().getName();
    }
}
