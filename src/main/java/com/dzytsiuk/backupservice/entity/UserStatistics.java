package com.dzytsiuk.backupservice.entity;

import lombok.Data;

@Data
public class UserStatistics {
    private User user;
    int userRequestCount;
    long userRequestMovieTypeCount;
    long userRequestUserTypeCount;
    long userRequestXlsxCount;
    long userRequestPdfCount;
}
