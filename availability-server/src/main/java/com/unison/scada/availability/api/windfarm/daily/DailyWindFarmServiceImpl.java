package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.availability.*;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DailyWindFarmServiceImpl implements DailyWindFarmService {

    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final MemoRepository memoRepository;


    @Override
    public DailyWindFarmDTO.Response getWindFarmGeneralInfo(LocalDateTime searchTime) {
        List<Memo> memos = memoRepository.findAllDataByTimeRange(searchTime, searchTime.plusHours(23).plusMinutes(59));

        //grouping memo by turbine id
        Map<Integer, List<Memo>> memoMap = memos.stream().collect(Collectors.groupingBy((data) -> data.getMemoId().getTurbineId()));
        List<Map<LocalDateTime, Memo>> data22 = new ArrayList<>();

        for(int turbineId = 1; turbineId <= windFarmProperties.getTurbinesNumber(); turbineId++) {
            Map<LocalDateTime, Memo> data = new HashMap<>();
            Optional<List<Memo>> optionalAvailabilityDataList = Optional.ofNullable(memoMap.get(turbineId));

            optionalAvailabilityDataList.ifPresent(availabilityDataList -> {
                for (Memo availabilityData : availabilityDataList) {
                    LocalDateTime hour = availabilityData.getMemoId().getTimestamp();
                    data.put(hour, availabilityData);
                }
            });

            data22.add(data);
        }

        int turbinesNumber = windFarmProperties.getTurbinesNumber();

        List<Map<LocalDateTime, List<AvailabilityData>>> listMap = getMap(searchTime);
        double numerator = 0; //분자
        double denominator = 0; //분모
        double etcTime = 0;

        List<DailyWindFarmDTO.Response.Turbine> turbineList = new ArrayList<>();

        for(int i = 0; i < listMap.size(); i++)
        {
            Map<LocalDateTime, List<AvailabilityData>> listMap2 = listMap.get(i);

            AnnuallyWindFarmServiceImpl.Avail avail = new AnnuallyWindFarmServiceImpl.Avail();
            List<DailyWindFarmDTO.Response.Data> dataList = new ArrayList<>();

            for(LocalDateTime key : listMap2.keySet()) {
                List<AvailabilityData> availabilityDatas = listMap2.get(key);

                List<DailyWindFarmDTO.Response.Availability> availabilities = new ArrayList<>();

                for(AvailabilityData availabilityData : availabilityDatas) {
                    avail.calcAvailability(availabilityData);

                    availabilities.add(DailyWindFarmDTO.Response.Availability.builder()
                            .name(availabilityData.getAvailabilityType().getName())
                            .time(availabilityData.getTime())
                            .build());
                }
                DailyWindFarmDTO.Response.Memo memo = null;
                Optional<Memo> optionalMemo = Optional.ofNullable(data22.get(i).get(key));

                if (optionalMemo.isPresent()) {
                    Memo memo1 = optionalMemo.get();
                    memo = new DailyWindFarmDTO.Response.Memo(memo1.getEngineerName(), memo1.getWorkTime(), memo1.getMaterial(), memo1.getQuantity(), memo1.getWorkType(), memo1.getInspection(), memo1.getEtc());
                }
                dataList.add(DailyWindFarmDTO.Response.Data.builder()
                        .time(key)
                        .memo(memo)
                        .availability(availabilities)
                        .build());
            }
            numerator += avail.getNumerator();
            denominator += avail.getDenominator();
            etcTime += avail.getEtcTime();

            Collections.sort(dataList);

            turbineList.add(DailyWindFarmDTO.Response.Turbine.builder()
                    .turbineId(i + 1)
                    .availability(avail.getAvail())
                    .data(dataList)
                    .build());
        }

        //get availability type list
        List<AvailabilityType> availabilityTypes = availabilityTypeRepository.findByActive(true);

        List<DailyWindFarmDTO.Response.AvailabilityStatus> availabilityStatus = new ArrayList<>();

        for(AvailabilityType availabilityType : availabilityTypes){
            availabilityStatus.add(DailyWindFarmDTO.Response.AvailabilityStatus.builder()
                            .name(availabilityType.getName())
                            .color(availabilityType.getColor())
                            .build());
        }


        return DailyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .date(searchTime)
                .availability(AnnuallyWindFarmServiceImpl.Avail.calcFormula(numerator, denominator, etcTime))
                .turbines(turbineList)
                .statusList(availabilityStatus)
                .build();
    }

    private List<Map<LocalDateTime, List<AvailabilityData>>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusHours(23).plusMinutes(59));

        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, List<AvailabilityData>>> turbines = new ArrayList<>();
        int turbinesNumber = windFarmProperties.getTurbinesNumber();

        Optional<AvailabilityType> informationUnavailableType = availabilityTypeRepository.findByName(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS);
        for(int turbineId = 1; turbineId <= turbinesNumber; turbineId++)
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
                List<AvailabilityData> availabilityDataList1 = new ArrayList<>();
                LocalDateTime clonedDateTime = LocalDateTime.of(startTime.getYear(),
                        startTime.getMonth(),
                        startTime.getDayOfMonth(),
                        startTime.getHour(),
                        startTime.getMinute(),
                        startTime.getSecond());

                LocalDateTime endTime;

                if(clonedDateTime.plusDays(1).isBefore(LocalDateTime.now()))
                    endTime = clonedDateTime.plusDays(1);
                else
                    endTime = LocalDateTime.now();

                while(clonedDateTime.isBefore(endTime)){
                    availabilityDataList1.add(
                            AvailabilityData.builder()
                                    .availabilityDataId(new AvailabilityData.AvailabilityDataId(startTime, turbineId, null))
                                    .time(3600)
                                    .availabilityType(informationUnavailableType.orElse(
                                            AvailabilityType.builder()
                                                    .name(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS)
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
            LocalDateTime clonedDateTime = LocalDateTime.of(startTime.getYear(),
                    startTime.getMonth(),
                    startTime.getDayOfMonth(),
                    startTime.getHour(),
                    startTime.getMinute(),
                    startTime.getSecond());

            LocalDateTime endTime;

            if(clonedDateTime.plusDays(1).isBefore(LocalDateTime.now()))
                endTime = clonedDateTime.plusDays(1);
            else
                endTime = LocalDateTime.now();

            while(clonedDateTime.isBefore(endTime)){
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
    private AvailabilityData returnInformationUnavailable(LocalDateTime timestamp, int turbineId){
        return AvailabilityData.builder()
                .availabilityDataId(new AvailabilityData.AvailabilityDataId(timestamp, turbineId, null))
                .time(3600)
                .availabilityType(AvailabilityType.builder()
                        .name(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS)
                        .description("")
                        .color("#C4D8F0")
                        .build())
                .build();
    }
}
