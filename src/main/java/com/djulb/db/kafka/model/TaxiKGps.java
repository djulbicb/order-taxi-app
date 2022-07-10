package com.djulb.db.kafka.model;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxiKGps {
    @Id
    private String id;
    private ObjectStatus status;
    private ObjectActivity activity;
    private Coordinate coordinate;
    @Field
    @Indexed(name="deleteAt", expireAfterSeconds=15)
    Date timestamp;
}
