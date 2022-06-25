package com.djulb.utils;

import java.time.Instant;

public class TimeUtils {
    public static long getNowEpoch() {
        return Instant.now().toEpochMilli();
    }
}
