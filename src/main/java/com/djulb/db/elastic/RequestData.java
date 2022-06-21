package com.djulb.db.elastic;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@Builder
public class RequestData {
    private String name;
    private double lat;
    private double lon;
}