package com.djulb.db.mongo;

import com.djulb.utils.ZoneService;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import static com.djulb.OrderTaxiAppSettings.MONGO_EXPIRE_AFTER_SECONDS;

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

    @Bean
    public MongoTemplate mongoMessageDb() throws Exception {
        String databaseName = "messageDb";
        MongoTemplate passangerTemplate = new MongoTemplate(mongo(), databaseName);
        passangerTemplate.indexOps(databaseName).ensureIndex(
            new Index().on("timestamp", Sort.Direction.ASC).expire(MONGO_EXPIRE_AFTER_SECONDS)
        );
        return passangerTemplate;
    }
}
