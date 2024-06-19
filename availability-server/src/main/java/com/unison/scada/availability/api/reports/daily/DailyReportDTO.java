package com.unison.scada.availability.api.reports.daily;

import com.unison.scada.availability.api.reports.memo.MemoReportDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class DailyReportDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private List<String> headerList;
        private List<DailyData> tableData;
        private LocalDateTime date;

        @Builder
        @Setter
        @Getter
        public static class DailyData {
            private String deviceName;
            private String dailyProduction;
            private String dailyAvailability;
            private String dailyCapacityFactor;
            private String monthlyProduction;
            private String monthlyCapacityFactor;
            private String monthlyAvailability;
        }
    }

    @Setter
    @Getter
    public static class Request{
        private String dailyDate;
    }
}
