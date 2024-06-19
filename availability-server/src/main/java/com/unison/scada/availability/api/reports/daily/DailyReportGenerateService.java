package com.unison.scada.availability.api.reports.daily;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

public interface DailyReportGenerateService {
    void generateDailyReportExcel(HttpServletResponse response, Principal principal) throws IOException;

}
