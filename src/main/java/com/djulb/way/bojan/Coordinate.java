package com.djulb.way.bojan;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private double lat;
    private double lng;

    public String formatted() {
        return String.format("%s,%s", lat, lng);
    }
}
