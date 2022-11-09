package com.djulb.engine.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PassangerIdGeneratorViewportObjectsGetByIds {

    @Test
    void getNext() {
        PassangerIdGenerator generator = new PassangerIdGenerator();
        String next = generator.getNext();
        String next1 = generator.getNext();

        Assertions.assertEquals("P-000001", next);
        Assertions.assertEquals("P-000002", next1);
    }
}