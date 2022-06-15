package com.djulb.db.mongo;

import com.djulb.way.elements.FakePerson;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoFakePersonRepository extends MongoRepository<FakePerson, String> {

}