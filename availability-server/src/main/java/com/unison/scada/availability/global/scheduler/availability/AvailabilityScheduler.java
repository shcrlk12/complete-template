package com.unison.scada.availability.global.scheduler.availability;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AvailabilityScheduler {

    @Scheduled(fixedRate = 2000)
    public void run(){

        System.out.println(LocalDateTime.now());
    }

}
