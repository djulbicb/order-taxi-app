package com.djulb.db.redis;

import com.djulb.common.objects.ObjectStatus;
import com.djulb.db.redis.model.RTaxiStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("redisTaxiStatusRepository")
public interface RTaxiStatusRepository extends CrudRepository<RTaxiStatus, String> {
    default List<RTaxiStatus> findAllByIdAndStatus(Iterable<String> ids, ObjectStatus status) {
        Iterable<RTaxiStatus> allById = findAllById(ids);
        List<RTaxiStatus> filteredByStatus = new ArrayList<>();
        for (RTaxiStatus rTaxiStatus : allById) {
            if(rTaxiStatus.getStatus() == status) {
                filteredByStatus.add(rTaxiStatus);
            }
        }
        return filteredByStatus;
    }

}
