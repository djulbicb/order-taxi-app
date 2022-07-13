package com.djulb.ui.routes;

import com.djulb.common.objects.ObjectType;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.KMContract;
import com.djulb.ui.routes.model.RouteUi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetRoutesUsecase {

    private final ContractServiceMRepository contractServiceMRepository;

    @GetMapping("/api/ui/routes/all")
    public List<RouteUi> createTaxiFromPlaceholder () throws IOException {
        return contractServiceMRepository.loadAllActiveContractsRoutes();
    }

    @GetMapping("/api/ui/routes/{type}/{id}")
    public RouteUi createTaxiFromPlaceholder (@PathVariable ObjectType type, @PathVariable String id) throws IOException {
        KMContract kmContract;
        if (type == ObjectType.TAXI) {
            kmContract = contractServiceMRepository.loadTaxiContract(id);
        } else {
            kmContract = contractServiceMRepository.loadPassangerContract(id);
        }
        if (kmContract == null) {
            return RouteUi.builder().pathTaxiToDestination(new ArrayList<>()).pathTaxiToPassanger(new ArrayList<>()).build();
        }
        return RouteUi.builder()
                .pathTaxiToPassanger(kmContract.getPathTaxiToPassanger())
                .pathTaxiToDestination(kmContract.getPathTaxiToDestination())
                .build();
    }
}
