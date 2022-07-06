package com.djulb.engine.generator;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ContractIdGenerator {
    private AtomicInteger next = new AtomicInteger(10);
    static NumberFormat formatter = new DecimalFormat("C-#00000");

    public String getNext() {
        int current = next.get();
        String format = formatter.format(current);
        next.set(current + 1);
        return format;
    }
}
