//package com.djulb.db.mongo;
//
//import com.djulb.way.elements.TaxiGps;
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.core.index.Index;
//
//@Configuration
//public class MongoTaxiConfig extends AbstractMongoClientConfiguration {
//    int expireAfterSeconds = 5;
//
//    public MongoClient mongo() {
//        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        return MongoClients.create(mongoClientSettings);
//    }
//    @Override
//    @Bean("mongoTaxiDb")
//    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
//
//        MongoTemplate taxi = new MongoTemplate(mongo(), "taxi");
//
//        taxi.indexOps("users").ensureIndex(
//                new Index().on("timestamp", Sort.Direction.ASC)
//                        .expire(expireAfterSeconds)
//        );
//        return taxi;
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return "taxi";
//    }
//}
