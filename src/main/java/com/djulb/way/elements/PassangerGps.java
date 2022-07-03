package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;



@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassangerGps implements Serializable {
    @Id
    private String id;
    private ObjectStatus status;
    private Coordinate coordinate;
    @Field
    @Indexed(name="deleteAt", expireAfterSeconds=15)
    Date timestamp;
}
