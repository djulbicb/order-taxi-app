package com.djulb.publishers.contracts;

import com.djulb.common.coord.BBox;
import com.djulb.publishers.contracts.model.ContractM;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ContractServiceMRepository {
    @Qualifier("mongoMessageDb")
    private final MongoTemplate mongoMessageDb;

    public ContractServiceMRepository(MongoTemplate mongoMessageDb) {
        this.mongoMessageDb = mongoMessageDb;
        System.out.println("sss");
        getMongoMessageDb();
    }

    public void getMongoMessageDb() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("Test"));

        Update update = new Update();

        update.addToSet("coordinateList", BBox.getBerlinBbox().getTopLeft());

        mongoMessageDb.upsert(query, update, ContractM.class);

        ContractM userTest5 = mongoMessageDb.findOne(query, ContractM.class);
        System.out.println("userTest5 - " + userTest5);
    }
}
