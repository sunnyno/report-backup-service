package com.dzytsiuk.backupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class BackupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackupServiceApplication.class, args);
    }

}