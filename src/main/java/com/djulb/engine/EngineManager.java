package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.engine.contract.Contract;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.engine.contract.steps.RNotificationService;
import com.djulb.engine.contract.steps._0HoldStep;
import com.djulb.engine.generator.ContractIdGenerator;
import com.djulb.engine.generator.PassangerIdGenerator;
import com.djulb.engine.generator.TaxiIdGenerator;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.ContractM;
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

import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_PASSENGER;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_TAXI;

@Component
@EnableScheduling

@Slf4j
public class EngineManager {

    private Map<String, Contract> mapContractsById = new HashMap<>();
    private final Map<String, Taxi> mapCarsById = new HashMap<>();
    private final Map<String, Passanger> mapPassangersById = new HashMap<>();

    private List<Taxi> addToRegisterCars = new ArrayList<>();
    private List<Passanger> addToRegisterPassanger = new ArrayList<>();
    private List<Taxi> removeFromRegisterCars = new ArrayList<>();
    private List<Contract> removeFromRegisterContract = new ArrayList<>();

    private final ContractFactory contractFactory;
    private final OsrmBackendApi osrmBackendApi;
    private final ContractServiceMRepository contractServiceMRepository;
    private final ZoneService zoneService;
    private final KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate;
    private final KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate;
    private final KafkaTemplate<String, ContractM> kafkaContractTemplate;
    private final PassangerIdGenerator passangerIdGenerator;
    private final TaxiIdGenerator taxiIdGenerator;
    private final ContractIdGenerator contractIdGenerator;
    protected final ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustom;
    protected final ElasticSearchRepository elasticSearchRepository;
    protected final RNotificationService notificationService;

    public EngineManager(OsrmBackendApi osrmBackendApi,
                         ZoneService zoneService,
                         KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate,
                         RNotificationService notificationService,
                         PassangerIdGenerator passangerIdGenerator,
                         KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate,
                         TaxiIdGenerator taxiIdGenerator,
                         ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustom,
                         ElasticSearchRepository elasticSearchRepository,
                         ContractServiceMRepository contractServiceMRepository,
                         KafkaTemplate<String, ContractM> kafkaContractTemplate,
                         ContractIdGenerator contractIdGenerator) {
        this.kafkaContractTemplate = kafkaContractTemplate;
        this.contractIdGenerator = contractIdGenerator;
        this.contractFactory = new ContractFactory(this, osrmBackendApi, notificationService, elasticSearchRepositoryCustom, elasticSearchRepository, contractServiceMRepository, kafkaPassangerTemplate, kafkaTaxiTemplate, this.kafkaContractTemplate);
        this.osrmBackendApi = osrmBackendApi;
        this.zoneService = zoneService;
        this.kafkaPassangerTemplate = kafkaPassangerTemplate;
        this.passangerIdGenerator = passangerIdGenerator;
        this.taxiIdGenerator = taxiIdGenerator;
        this.kafkaTaxiTemplate = kafkaTaxiTemplate;
        this.elasticSearchRepositoryCustom = elasticSearchRepositoryCustom;
        this.elasticSearchRepository = elasticSearchRepository;
        this.notificationService = notificationService;
        this.contractServiceMRepository = contractServiceMRepository;

        // delete db
        elasticSearchRepository.deleteAll();

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
                .step(new _0HoldStep(this.contractFactory, contractIdGenerator.getNext(), passanger, Duration.ofSeconds(5)))
                .build();
        mapContractsById.put(contract.getId(), contract);
    }

    @Scheduled(fixedDelay=1000)
    private void process() {
        // REMOVE TO BE REMOVED CARS
        if (removeFromRegisterCars.size() > 0) {
            for (Taxi taxi : removeFromRegisterCars) {
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

//            if (contract.getActive().getStatus() == AbstractContractStep.Status.FINISHED) {
//                addToRegisterRemoveContract(contract);
//            }
        }
        Instant end = Instant.now();
        System.out.println("Contract cycle finished. Duration is" + Duration.between(start, end));
    }

    private void addToRegisterRemoveContract(Contract contract) {
        removeFromRegisterContract.add(contract);;
    }

    public Taxi createCar() {
        Coordinate randomCoordinate = zoneService.getRandomCoordinate();
        return createCar(taxiIdGenerator.getNext(), randomCoordinate);
    }

    public Taxi createCar(Coordinate coordinate) {
        return createCar(taxiIdGenerator.getNext(), coordinate);
    }

    public Taxi createCar(String id, Coordinate coordinate) {
        Taxi car = Taxi.builder()
                .id(id)
                .status(ObjectStatus.IDLE)
                .activity(ObjectActivity.ACTIVE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();

//        elasticSearchRepository.save(objToElastic(car));
        return car;
    }


    private void populateInitialCarList() {
        int currentTaxiCount = mapCarsById.values().size();
        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_CARS - currentTaxiCount;
        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addToRegisterFakeCar(createCar());
        }
    }

    public void addToRegisterFakeCar(Taxi taxi) {
        addToRegisterCars.add(taxi);
    }
    public void addToRegisterFakeCar(List<Taxi> taxis) {
        addToRegisterCars.addAll(taxis);
    }
    public void addToRegisterPassanger(Passanger passanger) {
        addToRegisterPassanger.add(passanger);
    }
    public void addToRegisterPassanger(List<Passanger> passangers) {
        addToRegisterPassanger.addAll(passangers);
    }
    public void removeFromRegisterContract() {
        for (Contract contract : removeFromRegisterContract) {
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

}
