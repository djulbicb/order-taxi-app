package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.common.objects.*;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.engine.contract.Contract;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.engine.contract.steps._0HoldStep;
import com.djulb.engine.generator.PassangerIdGenerator;
import com.djulb.engine.generator.TaxiIdGenerator;
import com.djulb.common.coord.Coordinate;
import com.djulb.messages.redis.RedisNotification;
import com.djulb.messages.redis.RedisNotificationService;
import com.djulb.osrm.OsrmBackendApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_PASSENGER;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_TAXI;
import static com.djulb.common.objects.GpsConvertor.toGps;

@Component
@EnableScheduling

@Slf4j
public class EngineManager {
    private final ContractFactory contractFactory;
    private final OsrmBackendApi osrmBackendApi;
    private ArrayList<Contract>  contracts = new ArrayList<>();

    private final ZoneService zoneService;
    private final RedisNotificationService notificationService;
    private final KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate;
    private final ConcurrentHashMap<String, Passanger> passangersByIdMap = new ConcurrentHashMap<>();
    private final PassangerIdGenerator passangerIdGenerator;

    private final KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate;
    private final ConcurrentHashMap<String, Taxi> carsByIdMap = new ConcurrentHashMap<>();
    private final TaxiIdGenerator taxiIdGenerator;
    protected final ElasticSearchRepositoryCustomImpl foodPOIRepository;
    protected final ElasticSearchRepository repository;

    public EngineManager(OsrmBackendApi osrmBackendApi,
                         ZoneService zoneService,
                         RedisNotificationService notificationService,
                         KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate,
                         PassangerIdGenerator passangerIdGenerator,
                         KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate,
                         TaxiIdGenerator taxiIdGenerator,
                         ElasticSearchRepositoryCustomImpl foodPOIRepository,
                         ElasticSearchRepository repository) {
        this.contractFactory = new ContractFactory(this, osrmBackendApi, notificationService, foodPOIRepository, repository);
        this.osrmBackendApi = osrmBackendApi;
        this.zoneService = zoneService;
        this.notificationService = notificationService;
        this.kafkaPassangerTemplate = kafkaPassangerTemplate;
        this.passangerIdGenerator = passangerIdGenerator;
        this.taxiIdGenerator = taxiIdGenerator;
        this.kafkaTaxiTemplate = kafkaTaxiTemplate;
        this.foodPOIRepository = foodPOIRepository;
        this.repository = repository;

        // Car manager
//        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
//        String id = "test";
//        addFakeCar(createFakeCar(id, zoneService.getRandomCoordinateInZone(ZoneService.getZone(coordinate)).get()));
        populateCarList();


        // Passanger
        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
        Optional<Coordinate> coordinateInAdjecentZone = zoneService.getCoordinateInAdjecentZone(coordinate);
//        String id = "test";
//        addFakePassanger(createFakePassanger(id, coordinate, coordinateInAdjecentZone.get()));
        populatePassangerList();




        repository.deleteAll();

//        Taxi taxi = managerTaxi.getCarById("T-00001").get();

        Collection<Passanger> passangers = getPassangers();
        for (Passanger passanger : passangers) {
            Contract build = Contract.builder()
                    .person(passanger)
                    //.step(contractFactory.orderTaxi(passanger))
                    .step(new _0HoldStep(this.contractFactory, passanger, Duration.ofSeconds(5)))
                    .build();
            contracts.add(build);
        }
    }

    @Scheduled(fixedDelay=1000)
    private void populateList() {
        System.out.println("-----------" + LocalDateTime.now());

        Instant start = Instant.now();
        for (Contract contract : contracts) {
            contract.getActive().process();
        }
        Instant end = Instant.now();
        System.out.println("Contract cycle finished. Duration is" + Duration.between(start, end));
    }








    public Taxi createFakeCar() {
        Coordinate randomCoordinate = zoneService.getRandomCoordinate();
        return createFakeCar(taxiIdGenerator.getNext(), randomCoordinate);
    }

    public Taxi createFakeCar(Coordinate coordinate) {
        return createFakeCar(taxiIdGenerator.getNext(), coordinate);
    }

    public Taxi createFakeCar(String id, Coordinate coordinate) {
        Taxi car = Taxi.builder()
                .id(id)
                .status(ObjectStatus.IDLE)
                .activity(ObjectActivity.ACTIVE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();

        repository.save(objToElastic(car));
        return car;
    }


    public void addFakeCar(Taxi car) {
        carsByIdMap.put(car.getId().toString(), car);
    }

    public Optional<Taxi> getCarById(String id){
        return Optional.of(carsByIdMap.get(id));
    }
    public ConcurrentHashMap<String, Taxi> getCars() {
        return carsByIdMap;
    }

    //    @Scheduled(fixedDelay=5000)
    private void populateCarList() {
        int currentTaxiCount = carsByIdMap.values().size();
        boolean shouldAddMoreCars = OrderTaxiAppSettings.MINIMUM_CARS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_CARS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addFakeCar(createFakeCar());
        }
    }
    @Scheduled(fixedDelay=1000)
    private void updateTaxiGps() {
        for (Map.Entry<String, Taxi> entry : carsByIdMap.entrySet()) {
            String zone = entry.getKey();
            Taxi car = entry.getValue();

            // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_TAXI, car.getUuid().toString(), car.getCurrentPosition().formatted());
            //mongoTemplate.save(fakePerson, fakePerson.getZone());
            kafkaTaxiTemplate.send(TOPIC_GPS_TAXI, car.getId().toString(), toGps(car));
        }
        kafkaTaxiTemplate.flush();
        // force send - sync. Cause if it shuts down, it may not be sent
//        kafkaProducer.flush(); // close also does flush
//        kafkaProducer.close();
    }

    public Optional<Taxi> getTaxiByIds(List<String> taxiIds) {
        List<Taxi> taxis = new ArrayList<>();
        for (String id : taxiIds) {
            if (carsByIdMap.containsKey(id)) {
                taxis.add(carsByIdMap.get(id));
            }
        }
        if (taxis.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taxis.get(0));
    }





    public Passanger createFakePassanger() {
        Coordinate startCoordinate = zoneService.getRandomCoordinate();
        Optional<Coordinate> endCoordinate = zoneService.getCoordinateInAdjecentZone(startCoordinate);
        return createFakePassanger(passangerIdGenerator.getNext(), startCoordinate, endCoordinate.get());
    }

    public Passanger createFakePassanger(Coordinate startCoordinate) {
        Optional<Coordinate> endCoordinate = zoneService.getCoordinateInAdjecentZone(startCoordinate);
        return createFakePassanger(passangerIdGenerator.getNext(), startCoordinate, endCoordinate.get());
    }

    public Passanger createFakePassanger(String id, Coordinate startCoordinate, Coordinate endCoordinate) {
        notificationService.listPush("test", RedisNotification.builder().id(id).message("Passanger created " + id).build());
        Passanger person = Passanger.builder()
                .id(id)
                .status(ObjectStatus.IDLE)
                .activity(ObjectActivity.ACTIVE)
                .destination(endCoordinate)
                .currentPosition(startCoordinate)
                .build();
        return person;
    }

    public void addFakePassanger(Passanger car) {
        passangersByIdMap.put(car.getId(), car);
    }

    public Optional<Passanger> getPassangerById(String id){
        return Optional.of(passangersByIdMap.get(id));
    }

    public Collection<Passanger> getPassangers() {
        return passangersByIdMap.values();
    }

    //    @Scheduled(fixedDelay=5000)
    private void populatePassangerList() {
        int currentTaxiCount = passangersByIdMap.values().size();
        boolean shouldAddMoreCars = OrderTaxiAppSettings.MINIMUM_PASSENGERS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_PASSENGERS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addFakePassanger(createFakePassanger());
        }
    }
    @Scheduled(fixedDelay=1000)
    private void updatePassangerGps() {
        for (Map.Entry<String, Passanger> entry : passangersByIdMap.entrySet()) {
            String zone = entry.getKey();
            Passanger passanger = entry.getValue();

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PASSENGER, passanger.getId(), passanger.getCurrentPosition().formatted());
            //mongoTemplate.save(fakePerson, fakePerson.getZone());
//            kafkaProducer.send(record);
            kafkaPassangerTemplate.send(TOPIC_GPS_PASSENGER, passanger.getId(), toGps(passanger));

        }
        kafkaPassangerTemplate.flush();

        // force send - sync. Cause if it shuts down, it may not be sent
//        kafkaProducer.flush(); // close also does flush
    }

}
