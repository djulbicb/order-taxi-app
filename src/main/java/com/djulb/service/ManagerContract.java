package com.djulb.service;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.FoodPOIRepositoryCustomImpl;
import com.djulb.service.contract.Contract;
import com.djulb.service.contract.ContractFactory;
import com.djulb.service.contract.steps._0HoldStep;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.redis.RedisNotificationService;
import com.djulb.way.osrm.OsrmBackendApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Component
@EnableScheduling

@Slf4j
public class ManagerContract {
    private final ManagerPassanger managerPassanger;
    private final ManagerTaxi managerTaxi;
    private final ContractFactory contractFactory;
    private final OsrmBackendApi osrmBackendApi;
    private ArrayList<Contract>  contracts = new ArrayList<>();

    private final RedisNotificationService notificationService;
    protected final FoodPOIRepositoryCustomImpl foodPOIRepository;
    protected final FoodPOIRepository repository;

    public ManagerContract(ManagerPassanger managerPassanger, ManagerTaxi managerTaxi, ContractFactory contractFactory, OsrmBackendApi osrmBackendApi, RedisNotificationService notificationService, FoodPOIRepositoryCustomImpl foodPOIRepository, FoodPOIRepository repository) {
        this.managerPassanger = managerPassanger;
        this.managerTaxi = managerTaxi;
        this.contractFactory = contractFactory;
        this.osrmBackendApi = osrmBackendApi;
        this.notificationService = notificationService;
        this.foodPOIRepository = foodPOIRepository;
        this.repository = repository;

        repository.deleteAll();

//        Taxi taxi = managerTaxi.getCarById("T-00001").get();

        Collection<Passanger> passangers = managerPassanger.getPassangers();
        for (Passanger passanger : passangers) {
            Contract build = Contract.builder()
                    .person(passanger)
                    //.step(contractFactory.orderTaxi(passanger))
                    .step(new _0HoldStep(this.osrmBackendApi,  notificationService, foodPOIRepository, repository, passanger, Duration.ofSeconds(5), managerTaxi))
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
}
