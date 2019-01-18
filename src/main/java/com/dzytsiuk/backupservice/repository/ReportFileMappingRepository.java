package com.dzytsiuk.backupservice.repository;


import com.dzytsiuk.backupservice.entity.ReportFileMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportFileMappingRepository extends MongoRepository<ReportFileMapping, String> {
}
