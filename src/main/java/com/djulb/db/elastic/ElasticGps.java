package com.djulb.db.elastic;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@Document(indexName = "gps")
public class ElasticGps {

    public enum Type {
        TAXI, PASSANGER
    }
    public enum Status {
        IDLE, IN_PROCESS
    }

    @Id
    private String id;
    @Field(type = FieldType.Text)
    private Type type;
    @Field(type = FieldType.Text)
    private Status status;
    private GeoPoint location;

//    @Field(type = FieldType.Integer)
//    private Integer category;

}