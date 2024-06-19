package com.unison.scada.availability.api.reports.memo;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

public interface MemoReportGenerateService {

    void generateMemoReportExcel(HttpServletResponse response, Principal principal) throws IOException;
}
