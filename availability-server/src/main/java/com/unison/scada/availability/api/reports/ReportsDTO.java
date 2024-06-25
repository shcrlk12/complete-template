package com.unison.scada.availability.api.reports;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ReportsDTO {

    @Setter
    public static class Response{
        List<String> headers;
        List<TableRow> dataLists;

        public static class TableRow{
            List<Object> row;

            public void addData(Object data){
                row.add(data);
            }
        }
    }

    @Setter
    @Getter
    public static class Request{
        private String deviceType;
        private String windFarmName;
        private Integer turbineId;
        private String startDate;
        private String endDate;
        private String reportType;
    }

}
