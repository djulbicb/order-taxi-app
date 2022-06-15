package com.djulb.service;

import com.djulb.db.kafka.KafkaHandler;
import com.djulb.db.redis.RedisStudent;
import com.djulb.db.redis.StudentRepository;
import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.FakePerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.djulb.db.kafka.KafkaHandler.TOPIC_GPS_PERSON;

@Component
//@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class FakePersonManager implements ApplicationRunner {
    private final ZoneService zoneService;
    private final KafkaHandler kafkaHandler;

    private final MongoTemplate mongoTemplate;
    private final List<FakePerson> fakePersonMap = new ArrayList<>();

    private final StudentRepository studentRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        HashMap<String, List<Coordinate>> coordinatesWithZones = zoneService.getCoordinatesWithZones();

        for (Map.Entry<String, List<Coordinate>> entry : coordinatesWithZones.entrySet()) {
            String zone = entry.getKey();
            Coordinate coordinate = zoneService.getRandomCoordinateInZone(zone).get();

            FakePerson fakePerson = FakePerson.builder()
                    .currentPosition(coordinate)
                    .zone(zone)
                    .uuid(UUID.randomUUID().toString())
                    .build();
            fakePersonMap.add(fakePerson);
        }

        KafkaProducer producer = kafkaHandler.createKafkaProducer();
        for (FakePerson fakePerson : fakePersonMap) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PERSON, fakePerson.getUuid().toString(), fakePerson.getCurrentPosition().formatted());
            mongoTemplate.save(fakePerson);
            producer.send(record);
        }


        // force send - sync. Cause if it shuts down, it may not be sent
        producer.flush(); // close also does flush
        producer.close();

        log.info("Initial {} fake users created.", fakePersonMap.size());

        // Redis
        RedisStudent student = RedisStudent.builder().gender(RedisStudent.Gender.MALE).id("Eng2015001").name("John Doe").build();
        studentRepository.save(student);
        RedisStudent retrievedStudent =
                studentRepository.findById("Eng2015001").get();
    }
}
