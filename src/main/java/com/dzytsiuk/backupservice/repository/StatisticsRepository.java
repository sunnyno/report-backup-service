package com.dzytsiuk.backupservice.repository;

import com.dzytsiuk.backupservice.entity.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRepository extends MongoRepository<Statistics, String> {
}
