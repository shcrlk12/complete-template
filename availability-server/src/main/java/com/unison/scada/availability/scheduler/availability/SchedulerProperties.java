package com.unison.scada.availability.scheduler.availability;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scheduler")
@Getter
@Setter
public class SchedulerProperties {
    private Availability availability;

    @Getter
    @Setter
    public static class Availability{
        private int time;
    }
}
