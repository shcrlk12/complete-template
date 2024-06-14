package com.unison.scada.availability.api.reports;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

public interface MemoReportGenerateService {

    void memoReportExcelGenerate(HttpServletResponse response, Principal principal) throws IOException;
}
