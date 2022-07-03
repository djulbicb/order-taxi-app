package com.djulb.engine;

import com.djulb.engine.generator.TaxiIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaxiIdGeneratorViewportObjectsGetByIds {

    @Test
    void getNext() {
        TaxiIdGenerator generator = new TaxiIdGenerator();
        String next = generator.getNext();
        String next1 = generator.getNext();

        Assertions.assertEquals("T-00001", next);
        Assertions.assertEquals("T-00002", next1);
    }
}