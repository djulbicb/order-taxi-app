package com.djulb.service;

import com.djulb.kafka.KafkaHandler;
import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.FakePerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.djulb.kafka.KafkaHandler.TOPIC_GPS_PERSON;

@Component
//@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class FakePersonManager implements ApplicationRunner {
    private final ZoneService zoneService;
    private final KafkaHandler kafkaHandler;
    private final List<FakePerson> fakePersonMap = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        HashMap<String, List<Coordinate>> coordinatesWithZones = zoneService.getCoordinatesWithZones();

        for (Map.Entry<String, List<Coordinate>> entry : coordinatesWithZones.entrySet()) {
            String zone = entry.getKey();
            Coordinate coordinate = zoneService.getRandomCoordinateInZone(zone).get();

            FakePerson fakePerson = FakePerson.builder()
                    .currentPosition(coordinate)
                    .zone(zone)
                    .uuid(UUID.randomUUID())
                    .build();
            fakePersonMap.add(fakePerson);
        }

        KafkaProducer producer = kafkaHandler.createKafkaProducer();
        for (FakePerson fakePerson : fakePersonMap) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PERSON, fakePerson.getUuid().toString(), fakePerson.getCurrentPosition().formatted());
            producer.send(record);
        }


        // force send - sync. Cause if it shuts down, it may not be sent
        producer.flush(); // close also does flush
        producer.close();

        log.info("Initial {} users created", fakePersonMap.size());
    }
}
