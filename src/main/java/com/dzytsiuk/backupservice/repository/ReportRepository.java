package com.dzytsiuk.backupservice.repository;

import com.dzytsiuk.backupservice.entity.ReportRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<ReportRequest, Long> {
}
