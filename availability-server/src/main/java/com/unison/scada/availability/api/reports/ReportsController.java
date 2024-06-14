package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import com.unison.scada.availability.global.ReportExcelGenerator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//        String[] startDateSplits = startDate.split("_");
//        String[] endDateSplits = startDate.split("_");
//
//        LocalDateTime time = LocalDateTime.of(
//                Integer.parseInt(startDateSplits[0]),
//                Integer.parseInt(startDateSplits[1]),
//                Integer.parseInt(startDateSplits[2]),
//                0,
//                0);
//
//        System.out.println(startDate);

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
    ) {

        MemoReportDTO.Response response = reportsService.getMemoReportData(principal, request);


        return ResponseEntity.ok()
                .body(
                        JSONResponse.<MemoReportDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/memo/download/excel")
    public void memoExcelDownload(HttpServletResponse response, Principal principal) throws Exception {

        reportsService.memoReportExcelGenerate(response, principal);
    }
}
