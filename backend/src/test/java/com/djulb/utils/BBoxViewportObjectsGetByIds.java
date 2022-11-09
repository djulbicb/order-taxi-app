package com.djulb.utils;

import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.djulb.TestData.testBBox;

class BBoxViewportObjectsGetByIds {

    @Test
    void getMiddlePoint() {
        // given
        BBox bBox = testBBox();
        Coordinate expectedMiddlePoint = Coordinate.builder().lng(5d).lat(5d).build();

        // when
        Coordinate actualMiddlePoint = bBox.getMiddlePoint();

        // then
        Assertions.assertEquals(expectedMiddlePoint, actualMiddlePoint);
    }
}