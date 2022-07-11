package com.djulb.engine;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class EngineManagerStatistics {
    // Taxi status
    private static int taxiIdleCount = 0;
    private static int taxiInProcessCount =  0;

    // Taxi route length
    private static int taxiRouteCount = 0;
    private static double taxiRouteLengthTotal;
    private static double taxiRouteLengthAverage = 0;
    private static double taxiRouteLengthMinimum = 0;
    private static double taxiRouteLengthMaximum = 0;

    // Passanger status
    private static int passangerIdleCount = 0;
    private static int passangerRetryCount = 0;
    private static int passangerWaitingCount = 0;

    private static int passangerInProcessCount = 0;
    private static int passangerFinishedCount = 0;

    // Passanger waiting time
    private static long passangerWaitingTimeCount = 0;
    private static long passangerWaitingTimeTotal = 0;
    private static long passagnerWaitingTimeLongest = 0;
    private static long passagnerWaitingTimeAverage = 0;
    private static long passagnerWaitingTimeMinimum = 0;


    public static void taxiIdleIncr() {
        taxiIdleCount += 1;
    }

    public static void taxiIdleToInProcess() {
        taxiIdleCount -= 1;
        taxiInProcessCount += 1;
    }

    public static void taxiInProcessToIdle() {
        taxiIdleCount += 1;
        taxiInProcessCount -= 1;
    }

    public static void passangerIdleIncr() {
        passangerIdleCount += 1;
    }
    public static void passangerRetryIncr() {
        passangerRetryCount += 1;
    }
    public static void passangerIdleToWaiting() {
        passangerIdleCount -= 1;
        passangerWaitingCount += 1;
    }

    public static void passangerWaitingToInProcess() {
        passangerWaitingCount -= 1;
        passangerInProcessCount += 1;
    }

    public static void passangerInProcessToFinished() {
        passangerInProcessCount -= 1;
        passangerFinishedCount += 1;
    }

    public static void reset() {
        // Taxi status
        taxiIdleCount = 0;
        taxiInProcessCount =  0;

        // Taxi route length
        taxiRouteCount = 0;
        taxiRouteLengthTotal = 0;
        taxiRouteLengthAverage = 0;
        taxiRouteLengthMinimum = 0;
        taxiRouteLengthMaximum = 0;

        // Passanger status
        passangerIdleCount = 0;
        passangerRetryCount = 0;
        passangerWaitingCount = 0;
        passangerInProcessCount = 0;
        passangerFinishedCount = 0;

        // Passanger waiting time
        passangerWaitingTimeCount = 0;
        passangerWaitingTimeTotal = 0;
        passagnerWaitingTimeLongest = 0;
        passagnerWaitingTimeAverage = 0;
        passagnerWaitingTimeMinimum = 0;
    }

    public static LinkedHashMap<String, Object> getAsMap() throws IllegalAccessException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();;

        Field[] declaredFields = EngineManagerStatistics.class.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            Object fieldValue = field.get(null);

            map.put(fieldName, fieldValue);
        }
        return map;
    }

    public static void taxiRouteCalculate(double totalDistance) {
        taxiRouteLengthTotal += totalDistance;
        taxiRouteCount += 1;

        if (taxiRouteLengthMaximum < totalDistance) {
            taxiRouteLengthMaximum = totalDistance;
        }

        if (taxiRouteLengthMinimum > totalDistance) {
            taxiRouteLengthMinimum = totalDistance;
        }

        taxiRouteLengthAverage = taxiRouteLengthTotal / taxiRouteCount;
    }

    public static void passangerWaitTimeCalculate(long toSeconds) {
        passangerWaitingTimeTotal += toSeconds;
        passangerWaitingTimeCount += 1;

        if (passagnerWaitingTimeLongest < toSeconds) {
            passagnerWaitingTimeLongest = toSeconds;
        }

        if (passagnerWaitingTimeMinimum > toSeconds) {
            passagnerWaitingTimeMinimum = toSeconds;
        }

        passagnerWaitingTimeAverage = passangerWaitingTimeTotal / passangerWaitingTimeCount;
    }
}
