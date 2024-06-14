package com.unison.scada.availability.api.reports;

import java.security.Principal;

public interface StaticReportService {

    ReportsDTO.Response getStaticReportData(Principal principal, ReportsDTO.Request request);
}
