package com.unison.scada.availability.api.availability;

import com.unison.scada.availability.api.user.Error;
import com.unison.scada.availability.api.user.JSONResponse;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/real-time/")
public class RealTimeController {
//    public ResponseEntity<JSONResponse<RealTimeDTO.Response, Error>> getGeneralInfo(
//    ){
//
//        return ResponseEntity.ok()
//                .body(
//                        JSONResponse.<DailyWindFarmDTO.Response, Error>builder()
//                                .data(response)
//                                .build()
//                );
//
//    }
}
