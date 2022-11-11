package com.djulb.engine.generator;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PassangerIdGenerator {
    private AtomicInteger next = new AtomicInteger(1);
    static NumberFormat formatter = new DecimalFormat("P-#000000");

    public String getNext() {
        int current = next.get();
        String format = formatter.format(current);
        next.set(current + 1);
        return format;
    }
}