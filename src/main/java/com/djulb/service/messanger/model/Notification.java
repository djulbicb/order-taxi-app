package com.djulb.service.messanger.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "notification")
public class Notification {
    private final String id;
    private final String message;
}
