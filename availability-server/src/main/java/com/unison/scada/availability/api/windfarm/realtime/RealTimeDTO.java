package com.unison.scada.availability.api.windfarm.realtime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RealTimeDTO {

    @Getter
    @Builder
    public static class Response{
        private LocalDateTime timestamp;
        private double windSpeed;
        private double activePower;
    }
}
