package com.unison.scada.availability.api.windfarm.annually;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter

public class AnnuallyWindFarmDTO {

    @Getter
    @Builder
    public static class Response{
        private final int turbinesNumber;
        private final double availability;
        private final double capacityFactor;
        private final int yearsOfWarranty;
        private final LocalDateTime date;
        private final LocalDateTime startTimeOfYears;
        private final List<AnnuallyWindFarmDTO.Response.Turbine> turbines;

        @Getter
        @Builder
        @RequiredArgsConstructor
        public static class Turbine {
            private final int turbineId;
            private final double availability;
            private final List<AnnuallyWindFarmDTO.Response.Data> data;
        }

        @Getter
        @Builder
        @RequiredArgsConstructor
        public static class Data{
            private final LocalDateTime time;
            private final double availability;          //normal status or forced outage ...
        }
    }
}
