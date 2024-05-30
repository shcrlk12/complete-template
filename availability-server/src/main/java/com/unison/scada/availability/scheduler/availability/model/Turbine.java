package com.unison.scada.availability.scheduler.availability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    Map<String, Double> dataMap;

    public Turbine(int turbineId){
        this.turbineId = turbineId;
    }
}
