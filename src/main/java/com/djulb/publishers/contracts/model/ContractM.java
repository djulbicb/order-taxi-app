package com.djulb.publishers.contracts.model;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(indexName = "constracts")
public class ContractM {
    public enum Status {
        PASSANGER_IDLE,
        PASSANGER_SEARCHING,
        TAXI_PASSANGER_MATCHED,
        TAXI_PASSANGER_DRIVE_IN_PROGRESS,
        TAXI_PASSANGER_DRIVE_FINISHED,
        PASSANGER_CLOSE_CONTRACTD
    }
    @Id
    private final String contractId;
    @Field
    @Indexed(name="passangerId")
    private final String passangerId;
    @Field
    @Indexed(name="taxiId")
    private final String taxiId;
    @Indexed(name="taxiId")
    private final ObjectActivity activity;

    private Coordinate taxiStartPosition;
    private Coordinate passangerStartPosition;
    private Coordinate destination;

    private String routeSnapshotTaxi;
    private String routeSnapshotPassanger;

    private final List<Coordinate> coordinateList = new ArrayList<>();
}
