//package com.djulb.schedule;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
//@Configuration
//public class DynamicSchedulerConfig {
//    @Bean
//    public CustomDynamicSchedule getDynamicScheduler() {
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.initialize();
//        return  new CustomDynamicSchedule(threadPoolTaskScheduler);
//    }
//}