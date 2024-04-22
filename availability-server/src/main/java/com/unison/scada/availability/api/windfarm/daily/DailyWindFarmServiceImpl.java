package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.availability.AnnuallyWindFarmServiceImpl;
import com.unison.scada.availability.api.availability.AvailabilityData;
import com.unison.scada.availability.api.availability.AvailabilityDataRepository;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyWindFarmServiceImpl implements DailyWindFarmService {

    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;
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

        return DailyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .date(searchTime)
                .availability(AnnuallyWindFarmServiceImpl.Avail.calcFormula(numerator, denominator, etcTime))
                .turbines(turbineList)
                .build();
    }

    private List<Map<LocalDateTime, List<AvailabilityData>>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusHours(23).plusMinutes(59));

        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, List<AvailabilityData>>> turbines = new ArrayList<>();

        for(Integer turbineId : listMap.keySet())
        {
            Map<LocalDateTime, List<AvailabilityData>> data = new HashMap<>();
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
            turbines.add(data);
        }

        return turbines;
    }
}
