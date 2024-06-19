package com.unison.scada.availability.api.reports.memo;

import com.unison.scada.availability.api.reports.ReportsDTO;

import java.security.Principal;

public interface MemoReportService {
    MemoReportDTO.Response getMemoReportData(Principal principal, ReportsDTO.Request request) throws Exception;

}
