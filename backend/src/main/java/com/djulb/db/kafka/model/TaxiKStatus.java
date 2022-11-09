package com.djulb.db.kafka.model;

import com.djulb.common.objects.ObjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxiKStatus {
    private String id;
    private ObjectStatus status;
}
