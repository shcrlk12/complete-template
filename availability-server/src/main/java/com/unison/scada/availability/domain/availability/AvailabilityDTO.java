package com.unison.scada.availability.domain.availability;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AvailabilityDTO {
    private final int turbineId;
    private final LocalDateTime time;
    private final List<AvailabilityType> availabilityTypes;

    @Getter
    @Builder
    public static class AvailabilityType{
        private final String name;
        private final int time;
    }
}
