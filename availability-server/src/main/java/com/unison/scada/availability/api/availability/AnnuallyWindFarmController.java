package com.unison.scada.availability.api.availability;

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
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wind-farm/annually")
public class AnnuallyWindFarmController {
    private final AnnuallyWindFarmService annuallyWindFarmService;

    @GetMapping("/test")
    public void Test(){
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 17, 16, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 4, 17, 17, 0, 0);

        while (startTime.isBefore(endTime)) {
            // Generate UUID
            String uuid = UUID.randomUUID().toString();

            // Generate INSERT SQL statements
            String[] availabilityTypes = {"normal_uuid", "forced_uuid", "scheduled_uuid", "information_uuid", "requested_uuid", "etc_uuid"};
            Random random = new Random();
            int randomNumber1 = random.nextInt(3600 / 6);
            int randomNumber2 = random.nextInt(3600 / 6);
            int randomNumber3 = random.nextInt(3600 / 6);
            int randomNumber4 = random.nextInt(3600 / 6);
            int randomNumber5 = random.nextInt(3600 / 6);
            int randomNumber6 = 3600 - (randomNumber1 + randomNumber2 + randomNumber3 + randomNumber4 + randomNumber5);

            int[] times = {randomNumber6 , randomNumber1, randomNumber2, randomNumber3, randomNumber4, randomNumber5};

            for (int i = 0; i < availabilityTypes.length; i++) {
                String sql = String.format("INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) " +
                                "VALUES ('%s', 3, '%s', %d, @%s, CURRENT_TIMESTAMP());",
                        startTime, UUID.randomUUID().toString(), times[i], availabilityTypes[i]);
                System.out.println(sql);
            }

            // Move to the next hour
            startTime = startTime.plusHours(1);
        }
    }
    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<JSONResponse<AnnuallyWindFarmDTO.Response, Error>> getDailyWindFarmInfo(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day
    ){
        LocalDateTime searchTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        AnnuallyWindFarmDTO.Response response = annuallyWindFarmService.getWindFarmGeneralInfo(searchTime);

        return ResponseEntity.ok()
                .body(
                        JSONResponse.<AnnuallyWindFarmDTO.Response, Error>builder()
                                .data(response)
                                .build()
                );

    }

}
