package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import com.unison.scada.availability.api.windfarm.WindFarmController;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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
                );    }

}
