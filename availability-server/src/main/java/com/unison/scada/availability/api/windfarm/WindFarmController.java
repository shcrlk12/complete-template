package com.unison.scada.availability.api.windfarm;

import com.unison.scada.availability.api.BasicDTO;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeDTO;
import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import com.unison.scada.availability.scheduler.availability.update.TurbineDataUpdateByOPCService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wind-farm")
public class WindFarmController {
    Logger logger = LoggerFactory.getLogger(WindFarmController.class);

    private final WindFarmService windFarmService;

    @GetMapping("/annually/{year}")
    public ResponseEntity<JSONResponse<AnnuallyWindFarmDTO.Response, Error>> getAnnuallyWindFarmInfo(
            @PathVariable("year") int year
    ) throws Exception {
        LocalDateTime searchTime = LocalDateTime.of(year, 12, 31, 0, 0, 0);

        AnnuallyWindFarmDTO.Response response = windFarmService.getAnnuallyWindFarmGeneralInfo(searchTime);

        return ResponseEntity.ok()
                        .body(
                            JSONResponse.<AnnuallyWindFarmDTO.Response, Error>builder()
                                    .data(response)
                                    .build()
                        );
    }
    @GetMapping("/daily/{year}/{month}/{day}")
    public ResponseEntity<JSONResponse<DailyWindFarmDTO.Response, Error>> getDailyWindFarmInfo(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day
    ){
        LocalDateTime searchTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        DailyWindFarmDTO.Response response = windFarmService.getDailyWindFarmGeneralInfo(searchTime);

        return ResponseEntity.ok()
                        .body(
                            JSONResponse.<DailyWindFarmDTO.Response, Error>builder()
                                    .data(response)
                                    .build()
                        );

    }

    @PostMapping("/daily/register")
    public ResponseEntity<JSONResponse<BasicDTO.Response, Error>> registerDailyInfo(
            Principal principal,
            @RequestBody DailyWindFarmDTO.Request request
    ){
        try{
            windFarmService.registerDailyInfo(principal, request);
            return ResponseEntity.ok()
                    .body(JSONResponse.<BasicDTO.Response, Error>builder()
                            .data(new BasicDTO.Response(true, "test"))
                            .build());
        }
        catch(Exception e)
        {
            logger.error("error registering daily info", e);
            return ResponseEntity.internalServerError()
                    .body(JSONResponse.<BasicDTO.Response, Error>builder()
                                    .error(new Error(500, e.getMessage()))
                                    .build());
        }
    }

    @PostMapping("/daily/reset")
    public ResponseEntity<JSONResponse<BasicDTO.Response, Error>> resetDailyInfo(
            Principal principal,
            @RequestBody DailyWindFarmDTO.Request request
    ){
        try{
            windFarmService.resetDailyInfo(principal, request);
            return ResponseEntity.ok()
                    .body(JSONResponse.<BasicDTO.Response, Error>builder()
                            .data(new BasicDTO.Response(true, "test"))
                            .build());
        }
        catch(Exception e)
        {
            logger.error("error registering daily info", e);
            return ResponseEntity.internalServerError()
                    .body(JSONResponse.<BasicDTO.Response, Error>builder()
                            .error(new Error(500, e.getMessage()))
                            .build());
        }
    }
    @GetMapping("/realtime")
    public ResponseEntity<JSONResponse<RealTimeDTO.Response, Error>> getRealTimeData(){
        RealTimeDTO.Response response = windFarmService.getRealTimeData();

        return ResponseEntity.ok()
                .body(
                        JSONResponse.<RealTimeDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );
    }
}
