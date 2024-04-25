package com.unison.scada.availability.scheduler.availability.save;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;

import com.unison.scada.availability.scheduler.availability.model.AvailabilityTotalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AvailabilitySaveServiceImpl implements AvailabilitySaveService {
    private final AvailabilityTotalTime availabilityTotalTime;

    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;

    @Override
    public void save1HourAvailabilityTotalTime(int turbinesNumber) {
        List<AvailabilityData> availabilityDataList = new ArrayList<>();
        List<AvailabilityType> availabilityTypeList = availabilityTypeRepository.findByActive(true);

        for(int turbineId = 0; turbineId < turbinesNumber; turbineId++){

            Map<String, Integer> availabilityTypeAndTimeMapOfATurbine =
                    availabilityTotalTime.get1HourAvailabilityTimeByTurbineId(turbineId);

            for(String availabilityType : availabilityTypeAndTimeMapOfATurbine.keySet()){
                int totalTime = availabilityTypeAndTimeMapOfATurbine.get(availabilityType);

                LocalDateTime timeBefore1Hour = LocalDateTime.now().minusHours(1).withMinute(0).withSecond(0).withNano(0);

                availabilityDataList.add(AvailabilityData.builder()
                        .availabilityDataId(new AvailabilityData.AvailabilityDataId(timeBefore1Hour, turbineId, UUID.randomUUID()))
                        .availabilityType(findAvailabilityTypeByName(availabilityType, availabilityTypeList))
                        .time(totalTime)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }
        availabilityDataRepository.saveAll(availabilityDataList);

        availabilityTotalTime.cleanAllTotalTime();
    }

    private AvailabilityType findAvailabilityTypeByName(String name, List<AvailabilityType> availabilityTypes){
        for(AvailabilityType availabilityType : availabilityTypes) {
            if(availabilityType.getName().equalsIgnoreCase(name))
                return availabilityType;
        }
        return null;
    }
}
