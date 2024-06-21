package com.unison.scada.availability.scheduler.availability.model;

import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Turbine {

    int turbineId;
    AvailabilityStatus availabilityStatus;
    Map<String, Data> dataMap;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data{
        Double value;
        boolean isSave;
    }
    public Turbine(int turbineId){
        this.turbineId = turbineId;
    }
}
