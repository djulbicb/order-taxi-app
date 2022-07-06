package com.djulb.ui.contracts;

import com.djulb.common.objects.ObjectType;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.ContractM;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ShowContractUsecase {

    private final ContractServiceMRepository contractServiceMRepository;

    @GetMapping("/api/contract/{type}/{id}")
    public ContractM createTaxiFromPlaceholder (@PathVariable ObjectType type, @PathVariable String id) throws IOException {
        System.out.println(type);
        System.out.println(id);

        if (type == ObjectType.TAXI) {
            return contractServiceMRepository.loadTaxiContract(id);
        } else {
            return contractServiceMRepository.loadPassangerContract(id);
        }
    }
}
