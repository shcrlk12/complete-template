package com.unison.scada.availability.api.windfarm;

import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeDTO;
import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wind-farm")
public class WindFarmController {

    private final WindFarmService windFarmService;

    @GetMapping("/annually/{year}")
    public ResponseEntity<JSONResponse<AnnuallyWindFarmDTO.Response, Error>> getAnnuallyWindFarmInfo(
            @PathVariable("year") int year
    ){
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
