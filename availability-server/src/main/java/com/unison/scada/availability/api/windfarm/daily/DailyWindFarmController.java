package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wind-farm/daily")
public class DailyWindFarmController {

    private final DailyWindFarmService dailyWindFarmService;
    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<JSONResponse<DailyWindFarmDTO.Response, Error>> getDailyWindFarmInfo(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day
    ){
        LocalDateTime searchTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        DailyWindFarmDTO.Response response = dailyWindFarmService.getWindFarmGeneralInfo(searchTime);

        return ResponseEntity.ok()
                .body(
                JSONResponse.<DailyWindFarmDTO.Response, Error>builder()
                        .data(response)
                        .build()
        );

    }
}
