package com.djulb.ui.routes.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteUi {
    private List<Double[]> pathTaxiToPassanger;
    private List<Double[]> pathTaxiToDestination;
}
