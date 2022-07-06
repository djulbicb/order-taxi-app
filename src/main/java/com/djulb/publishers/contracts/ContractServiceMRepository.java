package com.djulb.publishers.contracts;

import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;
import com.djulb.publishers.contracts.model.ContractM;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ContractServiceMRepository {
    @Qualifier("mongoMessageDb")
    private final MongoTemplate mongoMessageDb;

    public ContractServiceMRepository(MongoTemplate mongoMessageDb) {
        this.mongoMessageDb = mongoMessageDb;
        System.out.println("sss");

        mongoMessageDb.dropCollection(ContractM.class);

        ContractM build = ContractM.builder()
                ._id("C-0111")
                .passangerId("P-123456")
                .coordinates(Collections.singletonList(BBox.getBerlinBbox().getMiddlePoint()))
                .build();

        ContractM build1 = ContractM.builder()
                ._id("C-0111")
                .coordinates(Collections.singletonList(BBox.getBerlinBbox().getTopLeft()))
                .build();

        updateFullContract(build);
    }

    //        update.addToSet("coordinateList", BBox.getBerlinBbox().getTopLeft());
//        update.set("taxiId", 10);
//        ContractM userTest5 = mongoMessageDb.findOne(query, ContractM.class);
//        System.out.println("userTest5 - " + userTest5);

    public UpdateResult updateFullContract(ContractM contractM) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(contractM.get_id()));

        Update update = contractToFullUpdate(contractM);

        return mongoMessageDb.upsert(query, update, ContractM.class);
    }

    public Update contractToFullUpdate(ContractM contractM) {
//        ObjectMapper oMapper = new ObjectMapper();
//        Map<String, Object> dataMap = oMapper.convertValue(contractM, Map.class);
//        dataMap.values().removeIf(Objects::isNull);

        Update update = new Update();

        if (contractM.get_id() != null) {
            update.set("_id", contractM.get_id());
        }
        if (contractM.getPassangerId() != null) {
            update.set("passangerId", contractM.getPassangerId());
        }
        if (contractM.getTaxiId() != null) {
            update.set("taxiId", contractM.getTaxiId());
        }
        if (contractM.getActivity() != null) {
            update.set("activity", contractM.getActivity());
        }
        if (contractM.getTaxiStartPosition() != null) {
            update.set("taxiStartPosition", contractM.getTaxiStartPosition());
        }
        if (contractM.getPassangerStartPosition() != null) {
            update.set("passangerStartPosition", contractM.getPassangerStartPosition());
        }
        if (contractM.getDestination() != null) {
            update.set("destination", contractM.getDestination());
        }
        if (contractM.getPathTaxiToPassanger() != null) {
            update.set("pathTaxiToPassanger", contractM.getPathTaxiToPassanger());
        }
        if (contractM.getPathTaxiToDestination() != null) {
            update.set("pathTaxiToDestination", contractM.getPathTaxiToDestination());
        }
        if (contractM.getCoordinates() != null && !contractM.getCoordinates().isEmpty()) {
            for (Coordinate coordinate : contractM.getCoordinates()) {
                update.addToSet("coordinates", coordinate);
            }
        }

        return update;
    }

    public ContractM loadPassangerContract(String passangerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("passangerId").is(passangerId));
        return mongoMessageDb.findOne(query, ContractM.class);
    }
    public ContractM loadTaxiContract(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("taxiId").is(id));
        return mongoMessageDb.findOne(query, ContractM.class);
    }
}
