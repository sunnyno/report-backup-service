package com.dzytsiuk.backupservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class GridFsConfig {
    private MongoDbFactory mongoDbFactory;
    private MappingMongoConverter mappingMongoConverter;

    @Bean
    public GridFsTemplate gridFsTemplate() {
        return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
    }

    @Autowired
    public void setMongoDbFactory(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
    }

    @Autowired
    public void setMappingMongoConverter(MappingMongoConverter mappingMongoConverter) {
        this.mappingMongoConverter = mappingMongoConverter;
    }
}
