package com.unison.scada.availability.api.reports.statics;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

public interface StaticReportGenerateService {
    void generateStaticReportExcel(HttpServletResponse response, Principal principal) throws IOException;

}
