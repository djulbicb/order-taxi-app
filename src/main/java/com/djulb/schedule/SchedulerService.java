package com.djulb.schedule;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.engine.EngineManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

// https://mbcoder.com/dynamic-task-scheduling-with-spring/
@Service
public class SchedulerService implements SchedulingConfigurer {

//    @Autowired
//    ConfigurationService    configurationService;


    private final EngineManager engineManager;

    public SchedulerService(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }

    Random rnd = new Random();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(poolScheduler());
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // Do not put @Scheduled annotation above this method, we don't need it anymore.
               System.out.println("---" + rnd.nextInt(10));

               engineManager.process();
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                Calendar nextExecutionTime = new GregorianCalendar();
                Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                nextExecutionTime.add(Calendar.MILLISECOND, OrderTaxiAppSettings.UPDATE_SPEED);
                return nextExecutionTime.getTime();
            }
        });
    }

}

