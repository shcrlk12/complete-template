package com.unison.scada.availability.api.windfarm.realtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RealTimeDTO {

    @Getter
    @Builder
    public static class Response{
        private LocalDateTime timestamp;
        private List<RealTime> dataList;

        @Getter
        @Builder
        public static class RealTime{
            private String name;
            @Setter
            private double value;
            @Setter
            private int base;
        }
    }

}
