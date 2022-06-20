package com.djulb.db.mongo;

import com.djulb.utils.ZoneService;
import com.djulb.way.elements.PassangerGps;
import com.djulb.way.elements.TaxiGps;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    private final ZoneService zoneService;

    int expireAfterSeconds = 5;
    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/test");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTaxiDb() throws Exception {
        MongoTemplate taxiTemplate = new MongoTemplate(mongo(), "taxi");

        for (String zone : zoneService.getZoneCoordinatesMap().keySet()) {
            taxiTemplate.indexOps(zone).ensureIndex(
                    new Index().on("timestamp", Sort.Direction.ASC)
                            .expire(expireAfterSeconds)
            );
        }

        return taxiTemplate;
    }

    @Bean
    public MongoTemplate mongoPassangerDb() throws Exception {
        MongoTemplate passangerTemplate = new MongoTemplate(mongo(), "passanger");
        for (String zone : zoneService.getZoneCoordinatesMap().keySet()) {
            passangerTemplate.indexOps(zone).ensureIndex(
                    new Index().on("timestamp", Sort.Direction.ASC)
                            .expire(expireAfterSeconds)
            );
        }
        return passangerTemplate;
    }
}
