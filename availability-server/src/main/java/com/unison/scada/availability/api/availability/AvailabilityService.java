package com.unison.scada.availability.api.availability;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.api.windfarm.WindFarmService;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class AvailabilityService {
    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final WindFarmProperties windFarmProperties;

    public List<Map<LocalDateTime, List<AvailabilityData>>> getDailyMap(LocalDateTime startTime, LocalDateTime endTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, endTime);

        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, List<AvailabilityData>>> turbines = new ArrayList<>();
        int turbinesNumber = windFarmProperties.getTurbinesNumber();

        Optional<AvailabilityType> informationUnavailableType = availabilityTypeRepository.findByName(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE);
        for(int turbineId = 0; turbineId < turbinesNumber; turbineId++)
        {
            Map<LocalDateTime, List<AvailabilityData>> data = new HashMap<>();

            if(listMap.containsKey(turbineId)) {
                List<AvailabilityData> availabilityDataList1 = listMap.get(turbineId);

                for(AvailabilityData availabilityData : availabilityDataList1)
                {
                    LocalDateTime hour = availabilityData.getAvailabilityDataId().getTimestamp();

                    if(!data.containsKey(hour)){
                        List<AvailabilityData> availabilityDataList2 = new ArrayList<>();
                        availabilityDataList2.add(availabilityData);

                        data.put(hour, availabilityDataList2);
                    }
                    else {
                        List<AvailabilityData> availabilityDataList2 = data.get(hour);
                        availabilityDataList2.add(availabilityData);
                    }
                }
            }else {
                LocalDateTime clonedDateTime = LocalDateTime.of(startTime.getYear(),
                        startTime.getMonth(),
                        startTime.getDayOfMonth(),
                        startTime.getHour(),
                        startTime.getMinute(),
                        startTime.getSecond());

                LocalDateTime endTime2;

                if(clonedDateTime.plusDays(1).isBefore(LocalDateTime.now()))
                    endTime2 = clonedDateTime.plusDays(1);
                else
                    endTime2 = LocalDateTime.now().minusHours(1);

                while(clonedDateTime.isBefore(endTime2)){
                    List<AvailabilityData> availabilityDataList1 = new ArrayList<>();

                    availabilityDataList1.add(
                            AvailabilityData.builder()
                                    .availabilityDataId(new AvailabilityData.AvailabilityDataId(startTime, 0, turbineId, null))
                                    .time(3600)
                                    .availabilityType(informationUnavailableType.orElse(
                                            AvailabilityType.builder()
                                                    .name(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE)
                                                    .description("")
                                                    .color("#C4D8F0")
                                                    .build()
                                    ))
                                    .build()
                    );
                    data.put(clonedDateTime, availabilityDataList1);

                    clonedDateTime = clonedDateTime.plusHours(1);
                }
            }

            //fill empty data of timestamp
            LocalDateTime clonedDateTime = cloneLocalDateTime(startTime);

            LocalDateTime endTime2;

            if(clonedDateTime.plusDays(1).isBefore(LocalDateTime.now()))
                endTime2 = clonedDateTime.plusDays(1);
            else
                endTime2 = LocalDateTime.now().minusHours(1);

            while(clonedDateTime.isBefore(endTime2)){
                if(!data.containsKey(clonedDateTime))
                {
                    LocalDateTime finalClonedDateTime = clonedDateTime;
                    int finalTurbineId = turbineId;

                    List<AvailabilityData> tempData = IntStream.range(0, 1)
                            .mapToObj((i) -> returnInformationUnavailable(finalClonedDateTime, finalTurbineId))
                            .collect(Collectors.toList());

                    data.put(clonedDateTime, tempData);
                }
                clonedDateTime = clonedDateTime.plusHours(1);
            }
            turbines.add(data);
        }
        return turbines;
    }


    public List<WindFarmService.Avail> getAvailabilityOfAllTurbine(LocalDateTime startTime, LocalDateTime endTime){
        List<WindFarmService.Avail> results = new ArrayList<>();

        List<Map<LocalDateTime, List<AvailabilityData>>> availabilityDataLists = getDailyMap(startTime, endTime);

        for(int turbineId = 0; turbineId < availabilityDataLists.size(); turbineId++)
        {
            WindFarmService.Avail avail = new WindFarmService.Avail();
            Map<LocalDateTime, List<AvailabilityData>> availabilityDataList = availabilityDataLists.get(turbineId);

            for(LocalDateTime localDateTime : availabilityDataList.keySet()) {
                List<AvailabilityData> availabilityDataListAtTime = availabilityDataList.get(localDateTime);

                for(AvailabilityData availabilityDataAtTime : availabilityDataListAtTime) {
                    avail.calcAvailability(availabilityDataAtTime);
                }
            }
            results.add(avail);
        }

        return results;
    }
    private LocalDateTime cloneLocalDateTime(LocalDateTime source){
        return LocalDateTime.of(source.getYear(),
                source.getMonth(),
                source.getDayOfMonth(),
                source.getHour(),
                source.getMinute(),
                source.getSecond());
    }

    private AvailabilityData returnInformationUnavailable(LocalDateTime timestamp, int turbineId){
        return AvailabilityData.builder()
                .availabilityDataId(new AvailabilityData.AvailabilityDataId(timestamp, 0, turbineId, null))
                .time(3600)
                .availabilityType(AvailabilityType.builder()
                        .name(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE)
                        .description("")
                        .color("#C4D8F0")
                        .build())
                .build();
    }
}
