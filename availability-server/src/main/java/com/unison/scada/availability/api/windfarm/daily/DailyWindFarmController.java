package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wind-farm/daily")
public class DailyWindFarmController {

    private final DailyWindFarmService dailyWindFarmService;
    @GetMapping("/general-info")
    public ResponseEntity<JSONResponse<DailyWindFarmDTO.Response, Error>> getDailyWindFarmInfo(){
        DailyWindFarmDTO.Response response = dailyWindFarmService.getWindFarmGeneralInfo();

        return ResponseEntity.ok()
                .body(
                JSONResponse.<DailyWindFarmDTO.Response, Error>builder()
                        .data(response)
                        .build()
        );

    }
}
