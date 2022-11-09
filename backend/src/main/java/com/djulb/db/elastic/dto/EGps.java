package com.djulb.db.elastic.dto;

import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.ObjectType;
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
public class EGps {
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