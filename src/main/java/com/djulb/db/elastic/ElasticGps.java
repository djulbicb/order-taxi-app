package com.djulb.db.elastic;

import com.djulb.way.elements.ObjectActivity;
import com.djulb.way.elements.ObjectStatus;
import com.djulb.way.elements.ObjectType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@Document(indexName = "gps")
public class ElasticGps {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private ObjectType type;
    @Field(type = FieldType.Text)
    private ObjectStatus status;
    @Field(type = FieldType.Text)
    private ObjectActivity activity;
    private GeoPoint location;

}