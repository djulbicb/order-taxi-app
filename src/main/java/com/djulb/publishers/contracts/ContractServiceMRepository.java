package com.djulb.publishers.contracts;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractServiceMRepository {
    @Qualifier("mongoMessageDb")
    private final MongoTemplate mongoMessageDb;

    public void getMongoMessageDb() {

    }
}
