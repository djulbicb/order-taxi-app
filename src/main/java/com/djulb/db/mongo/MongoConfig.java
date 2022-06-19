package com.djulb.db.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
public class MongoConfig {
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
        MongoTemplate taxi = new MongoTemplate(mongo(), "taxi");
        taxi.indexOps("users").ensureIndex(
                new Index().on("timestamp", Sort.Direction.ASC)
                        .expire(expireAfterSeconds)
        );
        return taxi;
    }

    @Bean
    public MongoTemplate mongoPassangerDb() throws Exception {
        return new MongoTemplate(mongo(), "passanger");
    }
}
