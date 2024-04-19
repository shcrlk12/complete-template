package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.availability.AnnuallyWindFarmServiceImpl;
import com.unison.scada.availability.api.availability.AvailabilityData;
import com.unison.scada.availability.api.availability.AvailabilityDataRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyWindFarmServiceImpl implements DailyWindFarmService {

    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;

    @Override
    public DailyWindFarmDTO.Response getWindFarmGeneralInfo(LocalDateTime searchTime) {

        int turbinesNumber = windFarmProperties.getTurbinesNumber();
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(searchTime, searchTime.plusDays(1));

        //Generate turbine JSON data
        List<DailyWindFarmDTO.Response.Turbine> turbineList = new ArrayList<>();

        Map<LocalDateTime, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTimestamp()));

        double numerator = 0; //분자
        double denominator = 0; //분모
        double etcTime = 0;

        for(int i = 0; i < turbinesNumber; i++){
            AnnuallyWindFarmServiceImpl.Avail avail = new AnnuallyWindFarmServiceImpl.Avail();
            List<DailyWindFarmDTO.Response.Data> dataList = new ArrayList<>();

            for(LocalDateTime key : listMap.keySet()) {
                List<AvailabilityData> availabilityDatas = listMap.get(key);

                List<DailyWindFarmDTO.Response.Availability> availabilities = new ArrayList<>();

                for(AvailabilityData availabilityData : availabilityDatas) {
                    avail.calcAvailability(availabilityData);

                    availabilities.add(DailyWindFarmDTO.Response.Availability.builder()
                                    .name(availabilityData.getAvailabilityType().getName())
                                    .time(availabilityData.getTime())
                                    .build());
                }

                dataList.add(DailyWindFarmDTO.Response.Data.builder()
                                .time(key)
                                .memo(null)
                                .availability(availabilities)
                                .build());
            }
            numerator += avail.getNumerator();
            denominator += avail.getDenominator();
            etcTime += avail.getEtcTime();

            turbineList.add(DailyWindFarmDTO.Response.Turbine.builder()
                            .turbineId(i + 1)
                            .availability(avail.getAvail())
                            .data(dataList)
                            .build());
        }

        return DailyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .date(LocalDateTime.now())
                .availability(AnnuallyWindFarmServiceImpl.Avail.calcFormula(numerator, denominator, etcTime))
                .turbines(turbineList)
                .build();
    }

    private List<Map<LocalDateTime, AnnuallyWindFarmServiceImpl.Avail>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusDays(1));
        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, AnnuallyWindFarmServiceImpl.Avail>> turbines = new ArrayList<>();

        for(int i = 1; i <= windFarmProperties.getTurbinesNumber(); i++)
        {
            Map<LocalDateTime, AnnuallyWindFarmServiceImpl.Avail> data = new HashMap<>();
            List<AvailabilityData> availabilityDataList1 = listMap.get(i);

            for(AvailabilityData availabilityData : availabilityDataList1)
            {
                LocalDateTime day = availabilityData.getAvailabilityDataId().getTimestamp();

                if(!data.containsKey(day)){
                    AnnuallyWindFarmServiceImpl.Avail a = new AnnuallyWindFarmServiceImpl.Avail();
                    a.calcAvailability(availabilityData);

                    data.put(day, a);
                }
                else {
                    AnnuallyWindFarmServiceImpl.Avail a = data.get(day);
                    a.calcAvailability(availabilityData);
                }
            }
            turbines.add(data);
        }

        return turbines;
    }


}
