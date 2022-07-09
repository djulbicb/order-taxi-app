package com.djulb.publishers.contracts.model;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String _id;
    @Field
    @Indexed(name="passangerId")
    private String passangerId;
    @Field
    @Indexed(name="taxiId")
    private String taxiId;
    @Indexed(name="taxiId")
    private ObjectActivity activity;

    private Coordinate taxiStartPosition;
    private Coordinate passangerStartPosition;
    private Coordinate destination;

    private List<Double[]> pathTaxiToPassanger;
    private List<Double[]> pathTaxiToDestination;

    private List<Coordinate> coordinates;
}
