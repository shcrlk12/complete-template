package com.unison.scada.availability.api.reports;

import lombok.Getter;
import lombok.Setter;

public class ReportsDTO {

    public static class Response{

    }

    @Setter
    @Getter
    public static class Request{
        String windFarmName;
        Integer turbineId;
        String startDate;
        String endDate;
        Integer reportType;
    }

}
