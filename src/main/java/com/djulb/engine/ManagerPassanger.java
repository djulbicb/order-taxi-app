package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.engine.generator.PassangerIdGenerator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.PassangerGps;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.redis.RedisNotification;
import com.djulb.way.elements.redis.RedisNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_PASSENGER;
import static com.djulb.way.elements.GpsConvertor.toGps;

@Component
@EnableScheduling
@Slf4j
public class ManagerPassanger {
    private final ZoneService zoneService;
    private final RedisNotificationService notificationService;
    private final KafkaTemplate<String, PassangerGps> kafkaTemplate;
    private final ConcurrentHashMap<String, Passanger> passangersByIdMap = new ConcurrentHashMap<>();
    private final PassangerIdGenerator generator;

    public ManagerPassanger(ZoneService zoneService, RedisNotificationService notificationService, KafkaTemplate<String, PassangerGps> kafkaTemplate, PassangerIdGenerator generator) {
        this.zoneService = zoneService;
        this.notificationService = notificationService;
        this.kafkaTemplate = kafkaTemplate;
        this.generator = generator;

        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
        Optional<Coordinate> coordinateInAdjecentZone = zoneService.getCoordinateInAdjecentZone(coordinate);
//        String id = "test";
//        addFakePassanger(createFakePassanger(id, coordinate, coordinateInAdjecentZone.get()));

        populateList();
    }

    private Passanger createFakePassanger() {
        Coordinate startCoordinate = zoneService.getRandomCoordinate();
        Optional<Coordinate> endCoordinate = zoneService.getCoordinateInAdjecentZone(startCoordinate);
        return createFakePassanger(generator.getNext(), startCoordinate, endCoordinate.get());
    }

    private Passanger createFakePassanger(String id, Coordinate startCoordinate, Coordinate endCoordinate) {
        notificationService.listPush("test", RedisNotification.builder().id(id).message("Passanger created " + id).build());
        Passanger person = Passanger.builder()
                .id(id)
                .status(Taxi.Status.IDLE)
                .destination(endCoordinate)
                .currentPosition(startCoordinate)
                .build();
        return person;
    }

    private void addFakePassanger(Passanger car) {
        passangersByIdMap.put(car.getId(), car);
    }

    public Optional<Passanger> getPassangerById(String id){
        return Optional.of(passangersByIdMap.get(id));
    }

    public Collection<Passanger> getPassangers() {
        return passangersByIdMap.values();
    }

//    @Scheduled(fixedDelay=5000)
    private void populateList() {
        int currentTaxiCount = passangersByIdMap.values().size();
        boolean shouldAddMoreCars = OrderTaxiAppSettings.MINIMUM_PASSENGERS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_PASSENGERS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addFakePassanger(createFakePassanger());
        }
    }
    @Scheduled(fixedDelay=1000)
    private void updateGps() {
        for (Map.Entry<String, Passanger> entry : passangersByIdMap.entrySet()) {
            String zone = entry.getKey();
            Passanger passanger = entry.getValue();

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PASSENGER, passanger.getId(), passanger.getCurrentPosition().formatted());
            //mongoTemplate.save(fakePerson, fakePerson.getZone());
//            kafkaProducer.send(record);
            kafkaTemplate.send(TOPIC_GPS_PASSENGER, passanger.getId(), toGps(passanger));

        }
        kafkaTemplate.flush();

        // force send - sync. Cause if it shuts down, it may not be sent
//        kafkaProducer.flush(); // close also does flush
    }



//    private final ZoneService zoneService;
//    private final KafkaHandler kafkaHandler;
//
//    private final MongoTemplate mongoTemplate;
//    private final List<FakePerson> fakePersonMap = new ArrayList<>();
//
//    private final StudentRepository studentRepository;
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        HashMap<String, List<Coordinate>> coordinatesWithZones = zoneService.getZoneCoordinatesMap();
//
//        for (Map.Entry<String, List<Coordinate>> entry : coordinatesWithZones.entrySet()) {
//            String zone = entry.getKey();
//            for (int i = 0; i < 2; i++) {
//                Coordinate coordinate = zoneService.getRandomCoordinateInZone(zone).get();
//
//                FakePerson fakePerson = FakePerson.builder()
//                        .currentPosition(coordinate)
//                        .zone(zone)
//                        .uuid(UUID.randomUUID().toString())
//                        .build();
//                fakePersonMap.add(fakePerson);
//            }
//        }
//
//        KafkaProducer producer = kafkaHandler.createKafkaProducer();
//        for (FakePerson fakePerson : fakePersonMap) {
//            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PERSON, fakePerson.getUuid().toString(), fakePerson.getCurrentPosition().formatted());
//            mongoTemplate.save(fakePerson, fakePerson.getZone());
//            producer.send(record);
//        }
//
//
//        // force send - sync. Cause if it shuts down, it may not be sent
//        producer.flush(); // close also does flush
//        producer.close();
//
//        log.info("Initial {} fake users created.", fakePersonMap.size());
//
//        // Redis
//        RedisStudent student = RedisStudent.builder().gender(RedisStudent.Gender.MALE).id("Eng2015001").name("John Doe").build();
//        studentRepository.save(student);
//        RedisStudent retrievedStudent =
//                studentRepository.findById("Eng2015001").get();
//    }
}
