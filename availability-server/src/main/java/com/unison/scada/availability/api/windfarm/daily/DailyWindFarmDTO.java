package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.availability.AvailabilityDTO;
import com.unison.scada.availability.api.memo.MemoDTO;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class DailyWindFarmDTO {

    @Getter
    @Builder
    public static class Response{
        private final int turbinesNumber;
        private final LocalDateTime date;
        private final double availability;
        private final List<Turbine> turbines;

        @Getter
        @Builder
        public static class Turbine {
            private final int turbineId;
            private final double availability;
            private final List<Data> data;
        }

        @Getter
        @Builder
        public static class Data{
            private final LocalDateTime time;
            private final Memo memo;
            private final List<Availability> availability;
        }

        @Getter
        @Builder
        public static class Memo{
            private String engineerName;
            private LocalDateTime workTime;
            private String material;
            private int quantity;
            private String workType;
            private String inspection;
            private String etc;

        }

        @Getter
        @Builder
        public static class Availability{
            private final String name;
            private final int time;
        }
    }
}
