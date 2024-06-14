package com.unison.scada.availability.api.reports;

import java.security.Principal;
import java.util.List;

public interface MemoReportService {
    MemoReportDTO.Response getMemoReportData(Principal principal, ReportsDTO.Request request) throws Exception;

}
