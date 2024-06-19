package com.unison.scada.availability.api.reports.daily;

import com.unison.scada.availability.api.reports.ReportsDTO;

import java.security.Principal;

public interface DailyReportService {
    DailyReportDTO.Response getDailyReportData(Principal principal, DailyReportDTO.Request request) throws Exception;

}
