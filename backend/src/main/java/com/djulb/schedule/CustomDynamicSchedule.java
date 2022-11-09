//package com.djulb.schedule;
//
//import com.djulb.OrderTaxiAppSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//
//import java.util.Date;
//import java.util.concurrent.ScheduledFuture;
//
//public class CustomDynamicSchedule extends DynamicSchedule implements Trigger {
//
//    private TaskScheduler taskScheduler;
//    private ScheduledFuture<?> schedulerFuture;
//    private Long delayInterval;
//
//    public CustomDynamicSchedule(TaskScheduler taskScheduler) {
//        this.taskScheduler = taskScheduler;
//    }
//
//    @Override
//    void change(Long milliseconds) {
////        this.delayInterval = milliseconds;
//        OrderTaxiAppSettings.UPDATE_SPEED = milliseconds;
//        schedulerFuture = taskScheduler.schedule(() -> { }, this);
//    }
//
//    @Override
//    public Date nextExecutionTime(TriggerContext triggerContext) {
//        Date lastTime = triggerContext.lastActualExecutionTime();
//        return (lastTime == null) ? new Date() : new Date(lastTime.getTime() + OrderTaxiAppSettings.UPDATE_SPEED);
//    }
//}
