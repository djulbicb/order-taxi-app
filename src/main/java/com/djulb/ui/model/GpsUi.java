package com.djulb.ui.model;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GpsUi {

    private String id;
    private ObjectType type;
    private ObjectActivity activity;
    private ObjectStatus status;
    private Coordinate coordinate;
    private Date timestamp;
}
