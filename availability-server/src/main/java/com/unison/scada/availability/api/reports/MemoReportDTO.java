package com.unison.scada.availability.api.reports;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class MemoReportDTO {
    @Builder
    @Getter
    public static class Response{
        private List<String> headerList;
        private List<MemoData> tableData;

        @Builder
        @Getter
        public static class MemoData{
            private String deviceName;
            private LocalDateTime timeStamp;
            private String engineerName;
            private LocalDateTime workTime;
            private String material;
            private Integer quantity;
            private String workType;
            private String inspection;
            private String etc;
        }
    }
}
