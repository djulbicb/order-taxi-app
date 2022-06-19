package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class PassangerGps implements Serializable {
    @Id
    private String id;
    private Passanger.Status status;
    private Coordinate coordinate;
    @Field
    @Indexed(name="deleteAt", expireAfterSeconds=60)
    String timestamp;
}