//package com.djulb.way.elements.redis;
//
//import com.djulb.way.bojan.Coordinate;
//import com.djulb.way.elements.Passanger;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//
//import java.io.Serializable;
//import java.util.Date;
//
//
//@RedisHash(value = "Passanger", timeToLive = 60)
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class PassangerRedisGps implements Serializable {
//    @Id
//    private String id;
//    private Passanger.Status status;
//    private Coordinate coordinate;
//    Date timestamp;
//}
