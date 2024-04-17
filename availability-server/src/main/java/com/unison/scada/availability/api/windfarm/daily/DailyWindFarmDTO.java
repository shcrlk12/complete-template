package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.availability.AvailabilityDTO;
import com.unison.scada.availability.api.memo.MemoDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class DailyWindFarmDTO {

    @Getter
    @Builder
    public static class Response{
        private final int turbineNumber;
        private final double dailyAvailability;
        private final LocalDateTime date;
        private final List<OneTurbineGeneralData> turbines;

        @Getter
        @Builder
        public static class OneTurbineGeneralData{
            private final int turbineId;
            private final double turbineAvailability;
            private final List<TurbineInfo> turbineInfos;
        }

        @Getter
        @Builder
        public static class TurbineInfo{
            private final LocalDateTime time;
            private final MemoDTO memo;
            private final AvailabilityDTO availability;
        }
    }
}
