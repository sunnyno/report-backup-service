package com.dzytsiuk.backupservice.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
public class ReportFileMapping {
    @Id
    private String fileId;
    private String fileName;
    private String requestId;
}
