package com.unison.scada.availability.api.reports.statics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class StaticReportDTO {

    @Setter
    @Getter
    public static class Response{
        private List<TableHeader> tableHeader;
        private TableDataRow tableData;

        @Setter
        @Getter
        @AllArgsConstructor
        public static class TableHeader{
            private String name;
            private String unit;
        }
        @Setter
        @Getter
        @Builder
        public static class TableDataRow{
            private List<TableDataItem> row;

            @Setter
            @Getter
            @Builder
            public static class TableDataItem{
                List<String> value;
            }
        }
    }
}
