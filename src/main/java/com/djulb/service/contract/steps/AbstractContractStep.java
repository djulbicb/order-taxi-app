package com.djulb.service.contract.steps;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.FoodPOIRepositoryCustomImpl;
import com.djulb.service.ManagerTaxi;
import com.djulb.way.elements.redis.RedisNotificationService;
import com.djulb.way.osrm.OsrmBackendApi;

public abstract class AbstractContractStep {
    protected final OsrmBackendApi osrmBackendApi;
    protected final RedisNotificationService notificationService;
    protected final FoodPOIRepositoryCustomImpl foodPOIRepository;
    protected final FoodPOIRepository repository;
    protected final ManagerTaxi managerTaxi;
    protected AbstractContractStep next;

    public AbstractContractStep(
            OsrmBackendApi osrmBackendApi,
            RedisNotificationService notificationService,
            FoodPOIRepository repository,
            FoodPOIRepositoryCustomImpl foodPOIRepository, ManagerTaxi managerTaxi) {
        this.osrmBackendApi = osrmBackendApi;
        this.notificationService = notificationService;
        this.foodPOIRepository = foodPOIRepository;
        this.repository = repository;
        this.managerTaxi = managerTaxi;

    }

    protected void addNext(AbstractContractStep step) {
        this.next = step;
    }
    private Status status = Status.IN_PROGRESS;

    public boolean hasNext() {
        return this.next != null;
    }

    public AbstractContractStep getNext() {
        return next;
    }

    enum Status {
         IN_PROGRESS, FINISHED
    }
    public void start() {

    };
    public void process() {

    };
    public void end() {

    };



    void setStatusInProgress() {
        this.status = Status.IN_PROGRESS;
    }
    void setStatusFinished() {
        this.status = Status.FINISHED;
    }

    boolean isFinished() {
        return this.status == Status.FINISHED;
    };
}
