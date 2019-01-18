package com.dzytsiuk.backupservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportParameter {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fromDate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime toDate;
}
