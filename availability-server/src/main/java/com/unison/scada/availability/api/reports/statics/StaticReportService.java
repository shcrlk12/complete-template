package com.unison.scada.availability.api.reports.statics;

import com.unison.scada.availability.api.reports.ReportsDTO;

import java.security.Principal;

public interface StaticReportService {

    StaticReportDTO.Response getStaticReportData(Principal principal, ReportsDTO.Request request) throws Exception;
}
