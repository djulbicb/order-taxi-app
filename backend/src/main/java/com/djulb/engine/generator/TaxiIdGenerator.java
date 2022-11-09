package com.djulb.engine.generator;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaxiIdGenerator {
    private AtomicInteger next = new AtomicInteger(1);
    static NumberFormat formatter = new DecimalFormat("T-#00000");

    public String getNext() {
        int current = next.get();
        String format = formatter.format(current);
        next.set(current + 1);
        return format;
    }
}
