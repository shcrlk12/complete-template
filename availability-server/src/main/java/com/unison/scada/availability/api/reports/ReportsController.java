package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.reports.daily.DailyReportDTO;
import com.unison.scada.availability.api.reports.memo.MemoReportDTO;
import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportsController {
    Logger logger = LoggerFactory.getLogger(ReportsController.class);

    private final ReportsService reportsService;

    @GetMapping("/static")
    public ResponseEntity<JSONResponse<ReportsDTO.Response, Error>> getStaticReport(
            Principal principal,
            @ModelAttribute ReportsDTO.Request request

            ) {

        ReportsDTO.Response response = reportsService.getStaticReportData(principal, request);

        return ResponseEntity.ok()
                .body(
                        JSONResponse.<ReportsDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );    }

    @GetMapping("/memo")
    public ResponseEntity<JSONResponse<MemoReportDTO.Response, Error>> getMemoReport(
            Principal principal,
            @ModelAttribute ReportsDTO.Request request
    ) throws Exception {

        MemoReportDTO.Response response = reportsService.getMemoReportData(principal, request);


        return ResponseEntity.ok()
                .body(
                        JSONResponse.<MemoReportDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/daily")
    public ResponseEntity<JSONResponse<DailyReportDTO.Response, Error>> getDailyReport(
            Principal principal,
            @ModelAttribute DailyReportDTO.Request request
    ) throws Exception {

        DailyReportDTO.Response response = reportsService.getDailyReportData(principal, request);


        return ResponseEntity.ok()
                .body(
                        JSONResponse.<DailyReportDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/memo/download/excel")
    public void memoExcelDownload(HttpServletResponse response, Principal principal) throws Exception {

        reportsService.generateMemoReportExcel(response, principal);
    }

    @GetMapping("/daily/download/excel")
    public void downloadDailyReportExcel(HttpServletResponse response, Principal principal) throws Exception {

        reportsService.generateDailyReportExcel(response, principal);
    }
}
