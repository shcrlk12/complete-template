package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.global.NullUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


public class DailyWindFarmDTO {

    @Getter
    @Setter
    public static class Request{
        private List<Turbine> timestamps;
        private Memo memo;
        private List<Availability> availability;

        @Getter
        @Setter
        public static class Turbine{
            private int turbineId; //여러개 가능
            private LocalDateTime timestamp; // 여러개 가능
        }
    }
    @Getter
    @Builder
    public static class Response{
        private final int turbinesNumber;
        private final LocalDateTime date;
        private final double availability;
        private final List<Turbine> turbines;
        private final List<AvailabilityStatus> statusList;

        @Getter
        @Builder
        public static class Turbine {
            private final int turbineId;
            private final double availability;
            private final List<Data> data;
        }

        @Getter
        @Builder
        public static class Data implements Comparable<Data>{
            private final LocalDateTime time;
            private final DailyWindFarmDTO.Memo memo;
            private final List<DailyWindFarmDTO.Availability> availability;
            private final boolean changed;

            @Override
            public int compareTo(Data data) {
                return time.compareTo(data.time);
            }
        }

        @Getter
        @Builder
        public static class AvailabilityStatus{
            private final String name;
            private final String color;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Memo {
        private String engineerName;
        private LocalDateTime workTime;
        private String material;
        private Integer quantity;
        private String workType;
        private String inspection;
        private String etc;

        public boolean isEmpty(){
            return NullUtil.ifNullIsEmpty(engineerName)
                    & NullUtil.ifNullIsEmpty(workTime)
                    & NullUtil.ifNullIsEmpty(material)
                    & NullUtil.ifNullIsEmpty(quantity)
                    & NullUtil.ifNullIsEmpty(workType)
                    & NullUtil.ifNullIsEmpty(inspection)
                    & NullUtil.ifNullIsEmpty(etc);
        }
    }

    @Getter
    @Builder
    public static class Availability {
        private final String name;
        private final double time;
    }
}
