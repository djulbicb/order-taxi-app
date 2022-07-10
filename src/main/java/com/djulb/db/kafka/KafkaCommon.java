package com.djulb.db.kafka;

public class KafkaCommon {
    // Topics
    public static final String TOPIC_GPS_PASSENGER = "gps_passenger";
    public static final String TOPIC_GPS_TAXI = "gps_taxi";
    public static final String TOPIC_STATUS_TAXI = "status_taxi";
    public static final String TOPIC_CONTRACT = "contracts";
    public static final String TOPIC_NOTIFICATIONS = "notifications";

    // Kafka setup
    public static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
}
