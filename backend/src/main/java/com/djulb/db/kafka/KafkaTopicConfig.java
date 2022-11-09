package com.djulb.db.kafka;

import com.djulb.db.kafka.KafkaCommon;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.djulb.db.kafka.KafkaCommon.*;

@Configuration
public class KafkaTopicConfig {

    @Value("${service.kafka.url}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);


        AdminClient adminClient = AdminClient.create(configs);
        adminClient.deleteTopics(List.of(TOPIC_GPS_TAXI, TOPIC_GPS_PASSENGER, TOPIC_CONTRACT, TOPIC_NOTIFICATIONS, TOPIC_STATUS_TAXI));

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicGpsPerson() {
        return TopicBuilder.name(TOPIC_GPS_PASSENGER)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofMinutes(1).toMillis()))
                .build();
    }
    @Bean
    public NewTopic topicGpsTaxi() {
        return TopicBuilder.name(TOPIC_GPS_TAXI)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofMinutes(1).toMillis()))
                .build();
    }
    @Bean
    public NewTopic topicContract() {
        return TopicBuilder.name(TOPIC_CONTRACT)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofMinutes(1).toMillis()))
                .build();
    }
    @Bean
    public NewTopic topicNotifications() {
        return TopicBuilder.name(TOPIC_NOTIFICATIONS)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofMinutes(1).toMillis()))
                .build();
    }

    @Bean
    public NewTopic topicStatusTaxi() {
        return TopicBuilder.name(TOPIC_STATUS_TAXI)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Duration.ofMinutes(1).toMillis()))
                .build();
    }
}
