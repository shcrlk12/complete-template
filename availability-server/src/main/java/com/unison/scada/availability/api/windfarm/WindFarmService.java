package com.unison.scada.availability.api.windfarm;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.parameter.Parameter;
import com.unison.scada.availability.api.parameter.ParameterRepository;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmService;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmService;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeDTO;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeService;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import com.unison.scada.availability.scheduler.availability.model.WindFarm;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WindFarmService implements DailyWindFarmService, AnnuallyWindFarmService, RealTimeService {
    private final ParameterRepository parameterRepository;
    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final MemoRepository memoRepository;
    private final WindFarm windFarm;
    @Override
    public DailyWindFarmDTO.Response getDailyWindFarmGeneralInfo(LocalDateTime searchTime) {
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

        List<Map<LocalDateTime, List<AvailabilityData>>> listMap = getDailyMap(searchTime);
        double numerator = 0; //분자
        double denominator = 0; //분모
        double etcTime = 0;

        List<DailyWindFarmDTO.Response.Turbine> turbineList = new ArrayList<>();

        for(int i = 0; i < listMap.size(); i++)
        {
            Map<LocalDateTime, List<AvailabilityData>> listMap2 = listMap.get(i);

            Avail avail = new Avail();
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
                .availability(Avail.calcFormula(numerator, denominator, etcTime))
                .turbines(turbineList)
                .statusList(availabilityStatus)
                .build();
    }

    private List<Map<LocalDateTime, List<AvailabilityData>>> getDailyMap(LocalDateTime startTime){
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
            LocalDateTime clonedDateTime = cloneLocalDateTime(startTime);

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
    @Override
    public AnnuallyWindFarmDTO.Response getAnnuallyWindFarmGeneralInfo(LocalDateTime searchTime) {
        List<Parameter> parameters = parameterRepository.findAll();

        Parameter parameter = parameters.get(0);

        Duration duration = Duration.between(parameter.getWarrantyDate(), searchTime);

        int howManyYearsOfWarranty = (int)(duration.toDays() / 365) + 1;

        LocalDateTime startTimeOfWarranty = parameter.getWarrantyDate().plusYears((duration.toDays() / 365));

        int turbinesNumber = windFarmProperties.getTurbinesNumber();
        List<Map<LocalDateTime, Avail>> turbineAvailTotal = getMap(startTimeOfWarranty);

        //set turbines
        List<AnnuallyWindFarmDTO.Response.Turbine> turbines = new ArrayList<>();
        double numerator = 0; //분자
        double denominator = 0; //분모
        double etcTime = 0;

        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i++) {
            Map<LocalDateTime, Avail> availMap = turbineAvailTotal.get(i);

            //set data
            List<AnnuallyWindFarmDTO.Response.Data> dataList = new ArrayList<>();
            LocalDateTime endTime = LocalDateTime.now().isBefore(startTimeOfWarranty.plusYears(1)) ? LocalDateTime.now() : startTimeOfWarranty.plusYears(1);

            for(LocalDateTime time = startTimeOfWarranty; time.isBefore(endTime); time = time.plusDays(1)){
                double turbineAvailability = 0;

                if(availMap.containsKey(time)){
                    Avail avail = availMap.get(time);

                    numerator += avail.getNumerator();
                    denominator += avail.getDenominator();
                    etcTime += avail.getEtcTime();

                    turbineAvailability = avail.getAvail();
                }

                dataList.add(new AnnuallyWindFarmDTO.Response.Data(time,  turbineAvailability));
            }

            double windFarmAvailability;

            try {
                windFarmAvailability = Avail.calcFormula(numerator, denominator, etcTime);
            }catch (Exception e){
                windFarmAvailability = 0;
            }

            turbines.add(new AnnuallyWindFarmDTO.Response.Turbine(i + 1, windFarmAvailability, dataList));
        }


        return AnnuallyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .yearsOfWarranty(howManyYearsOfWarranty)
                .startTimeOfYears(startTimeOfWarranty)
                .turbines(turbines)
                .date(searchTime)
                .build();
    }

    @Override
    public RealTimeDTO.Response getRealTimeData() {
        double sumActivePower = 0;
        double sumWindSpeed = 0;

        int turbinesNumber = windFarm.getTurbines().size();

        for(Turbine turbine : windFarm.getTurbines()){
            sumActivePower += turbine.getActivePower();
            sumWindSpeed += turbine.getWindSpeed();
        }

        return RealTimeDTO.Response.builder()
                .timestamp(LocalDateTime.now())
                .activePower(sumActivePower/turbinesNumber)
                .windSpeed(sumWindSpeed/turbinesNumber)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Avail{
        private double numerator; //분자
        private double denominator; //분모
        private double etcTime;

        public static double calcFormula(double numerator, double denominator, double etcTime){
            return (1 - (numerator / (etcTime - denominator))) * 100;
        }

        public static Avail returnZero(){
            return new Avail(1, 1, 2);
        }
        public double getAvail(){
            return Avail.calcFormula(numerator, denominator, etcTime);
        }

        public void calcAvailability(AvailabilityData availabilityData){

            if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.NORMAL_STATUS)){
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.FORCED_OUTAGE_STATUS))
            {
                numerator += availabilityData.getTime();
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS))
            {
                denominator += availabilityData.getTime();
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS))
            {

            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS))
            {
            }
            else
            {
            }
            etcTime += availabilityData.getTime();
        }

    }

    private List<Map<LocalDateTime, Avail>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusYears(1));
        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, Avail>> turbines = new ArrayList<>();

        for(int turbineId = 1; turbineId <= windFarmProperties.getTurbinesNumber(); turbineId++)
        {
            Map<LocalDateTime, Avail> data = new HashMap<>();
            if(listMap.containsKey(turbineId)) {
                List<AvailabilityData> availabilityDataList1 = listMap.get(turbineId);

                for (AvailabilityData availabilityData : availabilityDataList1) {
                    LocalDateTime day = availabilityData.getAvailabilityDataId().getTimestamp().withHour(0).withMinute(0).withSecond(0);

                    if (!data.containsKey(day)) {
                        Avail a = new Avail();
                        a.calcAvailability(availabilityData);

                        data.put(day, a);
                    } else {
                        Avail a = data.get(day);
                        a.calcAvailability(availabilityData);
                    }
                }
            }else{
                LocalDateTime indexTime = cloneLocalDateTime(startTime);
                LocalDateTime endTime;

                if(indexTime.plusYears(1).isBefore(LocalDateTime.now()))
                    endTime = indexTime.plusYears(1);
                else
                    endTime = LocalDateTime.now();

                while(indexTime.isBefore(endTime)){
                    if(!data.containsKey(indexTime))
                    {
                        data.put(indexTime, Avail.returnZero());
                    }
                    indexTime = indexTime.plusDays(1);
                }
            }
            turbines.add(data);
        }

        return turbines;
    }

    private int getHowManyYearsOfWarranty(LocalDateTime searchTime){

        return 0;
    }

    private LocalDateTime cloneLocalDateTime(LocalDateTime source){
        return LocalDateTime.of(source.getYear(),
                source.getMonth(),
                source.getDayOfMonth(),
                source.getHour(),
                source.getMinute(),
                source.getSecond());
    }
}