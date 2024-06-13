package com.unison.scada.availability.api.reports;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportsService implements StaticReportService, MemoReportService{
    @Override
    public ReportsDTO.Response getStaticReportData(ReportsDTO.Request request) {
        return null;
    }

    @Override
    public ReportsDTO.Response getMemoReportData(ReportsDTO.Request request) {

        String[] startDateSplits = request.startDate.split("_");
        String[] endDateSplits = request.startDate.split("_");

        LocalDateTime startDate = LocalDateTime.of(
                Integer.parseInt(startDateSplits[0]),
                Integer.parseInt(startDateSplits[1]),
                Integer.parseInt(startDateSplits[2]),
                0,
                0);

        LocalDateTime endDate = LocalDateTime.of(
                Integer.parseInt(endDateSplits[0]),
                Integer.parseInt(endDateSplits[1]),
                Integer.parseInt(endDateSplits[2]),
                0,
                0);

        return null;
    }
}
