package com.unison.scada.availability.api.availability;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealTimeDTO {

    @Getter
    @Builder
    public static class Response{
        private double windSpeed;
        private double activePower;
    }
}
