package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.db.kafka.model.NotificationK;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.db.redis.RTaxiStatusRepository;
import com.djulb.db.redis.model.RTaxiStatus;
import com.djulb.engine.contract.Contract;
import com.djulb.engine.contract.ContractHelper;
import com.djulb.engine.contract.steps.AbstractContractStep;
import com.djulb.engine.contract.steps._0HoldStep;
import com.djulb.engine.generator.ContractIdGenerator;
import com.djulb.engine.generator.PassangerIdGenerator;
import com.djulb.engine.generator.TaxiIdGenerator;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.KMContract;
import com.djulb.ui.placeholders.SampleService;
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
import java.util.concurrent.TimeUnit;

import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_PASSENGER;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_TAXI;
import static com.djulb.engine.EngineManagerStatistics.*;

@Component
@EnableScheduling

@Slf4j
public class EngineManager {

    private final ContractHelper contractHelper;
    private final SampleService sampleService;
    private Map<String, Contract> mapContractsById = new HashMap<>();
    private final Map<String, Taxi> mapCarsById = new HashMap<>();
    private final Map<String, Passanger> mapPassangersById = new HashMap<>();

    private List<Taxi> addToRegisterCars = new ArrayList<>();
    private List<Passanger> addToRegisterPassanger = new ArrayList<>();
    private List<Taxi> removeFromRegisterCarsList = new ArrayList<>();
    private List<Passanger> removeFromRegisterPassangerList = new ArrayList<>();
    private List<Contract> removeFromRegisterContractList = new ArrayList<>();
    private final OsrmBackendApi osrmBackendApi;
    private final ContractServiceMRepository contractServiceMRepository;
    private final ZoneService zoneService;
    private final KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate;
    private final KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate;
    private final KafkaTemplate<String, KMContract> kafkaContractTemplate;
    private final KafkaTemplate<String, NotificationK> kafkaNotificationTemplate;
    private final PassangerIdGenerator passangerIdGenerator;
    private final TaxiIdGenerator taxiIdGenerator;
    private final ContractIdGenerator contractIdGenerator;
    protected final ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustom;
    protected final ElasticSearchRepository elasticSearchRepository;
    private final RTaxiStatusRepository taxiStatusRepository;
    public EngineManager(OsrmBackendApi osrmBackendApi,
                         ZoneService zoneService,
                         KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate,
                         PassangerIdGenerator passangerIdGenerator,
                         KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate,
                         KafkaTemplate<String, NotificationK> kafkaNotificationTemplate, TaxiIdGenerator taxiIdGenerator,
                         ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustom,
                         ElasticSearchRepository elasticSearchRepository,
                         ContractServiceMRepository contractServiceMRepository,
                         KafkaTemplate<String, KMContract> kafkaContractTemplate,
                         ContractIdGenerator contractIdGenerator,
                         RTaxiStatusRepository taxiStatusRepository,
                         SampleService sampleService) {
        this.kafkaNotificationTemplate = kafkaNotificationTemplate;
        this.kafkaContractTemplate = kafkaContractTemplate;
        this.contractIdGenerator = contractIdGenerator;
        this.osrmBackendApi = osrmBackendApi;
        this.zoneService = zoneService;
        this.kafkaPassangerTemplate = kafkaPassangerTemplate;
        this.passangerIdGenerator = passangerIdGenerator;
        this.taxiIdGenerator = taxiIdGenerator;
        this.kafkaTaxiTemplate = kafkaTaxiTemplate;
        this.elasticSearchRepositoryCustom = elasticSearchRepositoryCustom;
        this.elasticSearchRepository = elasticSearchRepository;
        this.contractServiceMRepository = contractServiceMRepository;
        this.taxiStatusRepository = taxiStatusRepository;
        this.sampleService = sampleService;
        this.contractHelper = new ContractHelper(this, osrmBackendApi, kafkaNotificationTemplate, elasticSearchRepositoryCustom, elasticSearchRepository, taxiStatusRepository, contractServiceMRepository, kafkaPassangerTemplate, kafkaTaxiTemplate, kafkaContractTemplate);

        System.out.println(LocalDateTime.now() + " cars start");
        populateInitialCarList();
        System.out.println(LocalDateTime.now() + " cars end");
        populateInitialPassangerList();
        System.out.println(LocalDateTime.now() + " passanger end");
    }

    public void createContractFromPassanger(Passanger passanger) {
        Contract contract = Contract.builder()
                .id(contractIdGenerator.getNext())
                .person(passanger)
                //.step(contractFactory.orderTaxi(passanger))
                .step(new _0HoldStep( this.contractHelper, contractIdGenerator.getNext(), passanger, Duration.ofSeconds(5)))
                .build();
        mapContractsById.put(contract.getId(), contract);
    }



    // OVAJ RADI
    //@Scheduled(fixedDelay=1000)
    public void process() {
        sampleService.move();

        // REMOVE TO BE REMOVED CARS
        if (removeFromRegisterCarsList.size() > 0) {
            for (Taxi taxi : removeFromRegisterCarsList) {
                mapCarsById.remove(taxi.getId());
            }
        }
        // REMOVE CONTRACT AND PASSANGER FROM THE LIST
        removeFromRegisterContract();

        // Add
        if (addToRegisterPassanger.size() > 0) {
            for (Passanger passanger : addToRegisterPassanger) {
                createContractFromPassanger(passanger);
                mapPassangersById.put(passanger.getId(), passanger);
            }
            addToRegisterPassanger.clear();
        }
        if (addToRegisterCars.size() > 0) {
            for (Taxi taxi : addToRegisterCars) {
                mapCarsById.put(taxi.getId(), taxi);
            }
            addToRegisterCars.clear();
        }

        // PASSANGERS
        for (Map.Entry<String, Passanger> entry : mapPassangersById.entrySet()) {
            String zone = entry.getKey();
            Passanger passanger = entry.getValue();

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_PASSENGER, passanger.getId(), passanger.getCurrentPosition().formatted());
            kafkaPassangerTemplate.send(TOPIC_GPS_PASSENGER, passanger.getId(), toGps(passanger));

        }
//        kafkaPassangerTemplate.flush();

        // CARS
        for (Map.Entry<String, Taxi> entry : mapCarsById.entrySet()) {
            String zone = entry.getKey();
            Taxi car = entry.getValue();
            kafkaTaxiTemplate.send(TOPIC_GPS_TAXI, car.getId().toString(), toGps(car));
        }
//        kafkaTaxiTemplate.flush();

        // CONTRACTS
        System.out.println("-----------" + LocalDateTime.now());

        Instant start = Instant.now();
        for (Contract contract : mapContractsById.values()) {
            contract.getActive().process();

            if (contract.getActive().getStatus() == AbstractContractStep.Status.TO_BE_REMOVED) {
                addToRegisterRemoveContract(contract);
            }
        }
        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        processingTime(TimeUnit.NANOSECONDS.toMillis(between.getNano()));
        System.out.println("Contract cycle finished. Duration is" + between);
    }

    private void addToRegisterRemoveContract(Contract contract) {
        removeFromRegisterContractList.add(contract);;
    }

    public Taxi createCar() {
        Coordinate randomCoordinate = zoneService.getRandomCoordinate();
        return createCar(taxiIdGenerator.getNext(), randomCoordinate);
    }

    public Taxi createCar(Coordinate coordinate) {
        return createCar(taxiIdGenerator.getNext(), coordinate);
    }

    public Taxi createCar(String id, Coordinate coordinate) {
        EngineManagerStatistics.taxiIdleIncr();
        Taxi car = Taxi.builder()
                .id(id)
                .status(ObjectStatus.IDLE)
                .activity(ObjectActivity.ACTIVE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();
        incrTotalTaxi();
        return car;
    }


    private void populateInitialCarList() {
        int currentTaxiCount = mapCarsById.values().size();
        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_CARS - currentTaxiCount;
        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addToRegisterCar(createCar());
        }
    }

    public void addToRegisterCar(Taxi taxi) {
        addToRegisterCars.add(taxi);
        taxiStatusRepository.save(RTaxiStatus.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .build());
    }
    public void addToRegisterCar(List<Taxi> taxis) {
        addToRegisterCars.addAll(taxis);
    }
    public void addToRegisterPassanger(Passanger passanger) {
        addToRegisterPassanger.add(passanger);
        incrTotalPassanger();
    }
    public void addToRegisterPassanger(List<Passanger> passangers) {
        addToRegisterPassanger.addAll(passangers);
    }
    public void removeFromRegisterContract() {
        for (Contract contract : removeFromRegisterContractList) {
            if (mapContractsById.containsKey(contract.getId())) {
                mapContractsById.remove(contract.getId());
                mapPassangersById.remove(contract.getPerson());
            }
        }
    }



    public Optional<Taxi> getTaxiByIds(List<String> taxiIds) {
        List<Taxi> taxis = new ArrayList<>();
        for (String id : taxiIds) {
            if (mapCarsById.containsKey(id)) {
                Taxi taxi = mapCarsById.get(id);
                if (taxi.getStatus() == ObjectStatus.IDLE) {
                    taxis.add(taxi);
                }
            }
        }
        if (taxis.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taxis.get(0));
    }

    public Passanger createPassanger() {
        Coordinate startCoordinate = zoneService.getRandomCoordinate();
        Optional<Coordinate> endCoordinate = zoneService.getCoordinateInAdjecentZone(startCoordinate);
        return createPassanger(passangerIdGenerator.getNext(), startCoordinate, endCoordinate.get());
    }

    public Passanger createPassanger(Coordinate startCoordinate) {
        Optional<Coordinate> endCoordinate = zoneService.getCoordinateInAdjecentZone(startCoordinate);
        return createPassanger(passangerIdGenerator.getNext(), startCoordinate, endCoordinate.get());
    }

    public Passanger createPassanger(String id, Coordinate startCoordinate, Coordinate endCoordinate) {
        EngineManagerStatistics.passangerIdleIncr();
        Passanger person = Passanger.builder()
                .id(id)
                .status(ObjectStatus.IDLE)
                .activity(ObjectActivity.ACTIVE)
                .destination(endCoordinate)
                .currentPosition(startCoordinate)
                .build();
        return person;
    }


    //    @Scheduled(fixedDelay=5000)
    private void populateInitialPassangerList() {
        int currentTaxiCount = mapPassangersById.values().size();
        boolean shouldAddMoreCars = OrderTaxiAppSettings.MINIMUM_PASSENGERS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_PASSENGERS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addToRegisterPassanger(createPassanger());
        }
    }

    public void deleteAll() {
        removeFromRegisterCarsList.addAll(mapCarsById.values());
        removeFromRegisterContractList.addAll(mapContractsById.values());
        mapPassangersById.clear();
        EngineManagerStatistics.reset();
    }
}