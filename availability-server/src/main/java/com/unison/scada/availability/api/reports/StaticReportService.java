package com.unison.scada.availability.api.reports;

public interface StaticReportService {

    ReportsDTO.Response getStaticReportData(ReportsDTO.Request request);
}
