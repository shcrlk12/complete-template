package com.unison.scada.availability.api.windfarm;

import com.unison.scada.availability.api.availability.AvailabilityService;
import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.availability.variable.ConstantVariable;
import com.unison.scada.availability.api.availability.variable.Variable;
import com.unison.scada.availability.api.availability.variable.VariableRepository;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.parameter.Parameter;
import com.unison.scada.availability.api.parameter.ParameterRepository;
import com.unison.scada.availability.api.reports.RowData;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.annually.AnnuallyWindFarmService;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmService;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeDTO;
import com.unison.scada.availability.api.windfarm.realtime.RealTimeService;
import com.unison.scada.availability.comm.opcda.OPCServer;
import com.unison.scada.availability.global.General;
import com.unison.scada.availability.global.OriginalAvailabilityData;
import com.unison.scada.availability.global.filter.GeneralRepository;
import com.unison.scada.availability.global.filter.OriginalAvailabilityDataRepository;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import com.unison.scada.availability.scheduler.availability.model.WindFarm;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WindFarmService implements DailyWindFarmService, AnnuallyWindFarmService, RealTimeService {
    static Logger logger = LoggerFactory.getLogger(WindFarmService.class);

    private final ParameterRepository parameterRepository;
    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final MemoRepository memoRepository;
    private final WindFarm windFarm;
    private final GeneralRepository generalRepository;
    private final OriginalAvailabilityDataRepository originalAvailabilityDataRepository;
    private final AvailabilityService availabilityService;
    private final VariableRepository variableRepository;

    @Override
    public DailyWindFarmDTO.Response getDailyWindFarmGeneralInfo(LocalDateTime searchTime) {
        List<Memo> memos = memoRepository.findAllDataByTimeRange(searchTime, searchTime.plusHours(23).plusMinutes(59));

        //grouping memo by turbine id
        Map<Integer, List<Memo>> memoMap = memos.stream().collect(Collectors.groupingBy((data) -> data.getMemoId().getTurbineId()));
        List<Map<LocalDateTime, Memo>> data22 = new ArrayList<>();

        for(int turbineId = 0; turbineId < windFarmProperties.getTurbinesNumber(); turbineId++) {
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

        List<Map<LocalDateTime, List<AvailabilityData>>> listMap = availabilityService.getDailyMap(searchTime, searchTime.plusHours(23).plusMinutes(59));
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
                boolean isChanged = false;
                List<AvailabilityData> availabilityDatas = listMap2.get(key);

                List<DailyWindFarmDTO.Availability> availabilities = new ArrayList<>();

                for(AvailabilityData availabilityData : availabilityDatas) {
                    if(availabilityData.getUpdatedAt() != null)
                        isChanged = true;
                    avail.calcAvailability(availabilityData);

                    availabilities.add(DailyWindFarmDTO.Availability.builder()
                            .name(availabilityData.getAvailabilityType().getName())
                            .time(availabilityData.getTime())
                            .build());
                }
                availabilities.sort(new Comparator<DailyWindFarmDTO.Availability>() {
                    @Override
                    public int compare(DailyWindFarmDTO.Availability a1, DailyWindFarmDTO.Availability a2) {
                        return a1.getName().compareTo(a2.getName());
                    }
                });
                DailyWindFarmDTO.Memo memo = null;
                Optional<Memo> optionalMemo = Optional.ofNullable(data22.get(i).get(key));

                if (optionalMemo.isPresent()) {
                    Memo memo1 = optionalMemo.get();
                    memo = new DailyWindFarmDTO.Memo(memo1.getEngineerName(), memo1.getWorkTime(), memo1.getMaterial(), memo1.getQuantity(), memo1.getWorkType(), memo1.getInspection(), memo1.getEtc());
                }


                dataList.add(DailyWindFarmDTO.Response.Data.builder()
                        .time(key)
                        .memo(memo)
                        .availability(availabilities)
                        .changed(isChanged)
                        .build());
            }
            numerator += avail.getNumerator();
            denominator += avail.getDenominator();
            etcTime += avail.getEtcTime();

            Collections.sort(dataList);

            turbineList.add(DailyWindFarmDTO.Response.Turbine.builder()
                    .turbineId(i)
                    .availability(avail.getAvail())
                    .data(dataList)
                    .build());
        }

        //get availability type list
        List<AvailabilityType> availabilityTypes = availabilityTypeRepository.findByIsActiveTrue();

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

    @Override
    public void resetDailyInfo(Principal principal, DailyWindFarmDTO.Request request) throws Exception {
//        for(int i = 0; i < request.getTimestamps().size(); i++) {
//            DailyWindFarmDTO.Request.Turbine timestamp = request.getTimestamps().get(i);
//
//            List<AvailabilityData> availabilityDataList = availabilityDataRepository.findByIdWithoutUUID(timestamp.getTimestamp().plusHours(9), timestamp.getTurbineId());
//            if(availabilityDataList.isEmpty()){
//                continue;
//            }
//
//            availabilityDataRepository.save(null);
//        }

//        originalAvailabilityDataRepository.findByOriginalAvailabilityDataIdTimestampAndOriginalAvailabilityDataId
    }

    @Override
    public void registerDailyInfo(Principal principal, DailyWindFarmDTO.Request request) throws Exception{
        List<Memo> memoList = new ArrayList<>();

        for(int i = 0; i < request.getTimestamps().size(); i++){
            DailyWindFarmDTO.Request.Turbine turbine = request.getTimestamps().get(i);
            DailyWindFarmDTO.Memo memo = request.getMemo();
            List<DailyWindFarmDTO.Availability> availabilityList = request.getAvailability();

            if(!memo.isEmpty()){
                memoRepository.save(Memo.builder().memoId(
                                Memo.MemoId.builder()
                                        .timestamp(turbine.getTimestamp().plusHours(9))
                                        .windFarmId(0)
                                        .turbineId(turbine.getTurbineId())
                                        .build())
                        .engineerName(memo.getEngineerName())
                        .workTime(memo.getWorkTime())
                        .material(memo.getMaterial())
                        .quantity(memo.getQuantity())
                        .workType(memo.getWorkType())
                        .inspection(memo.getInspection())
                        .etc(memo.getEtc())
                        .isActive(true)
                        .isDelete(false)
                        .createdAt(LocalDateTime.now())
                        .createdBy(principal.getName())
                        .build());
            }


            //find
            List<AvailabilityData> availabilityDataList = availabilityDataRepository.findByIdWithoutUUID(turbine.getTimestamp().plusHours(9), turbine.getTurbineId());
            List<AvailabilityType> availabilityTypeList = availabilityTypeRepository.findByIsActiveTrue();

            for(DailyWindFarmDTO.Availability availability : availabilityList){

                Optional<AvailabilityData> optionalAvailabilityData = availabilityDataList.stream()
                        .filter(data -> data.getAvailabilityType().getName().equalsIgnoreCase(availability.getName()))
                        .findFirst();

                //기존 저장되어 있는 데이터가 있을때
                if(optionalAvailabilityData.isPresent())
                {
                    AvailabilityData availabilityData = optionalAvailabilityData.get();

                    originalAvailabilityDataRepository.save(OriginalAvailabilityData.builder()
                            .originalAvailabilityDataId(new OriginalAvailabilityData.OriginalAvailabilityDataId(
                                    availabilityData.getAvailabilityDataId().getTimestamp(),
                                    availabilityData.getAvailabilityDataId().getWindFarmId(),
                                    availabilityData.getAvailabilityDataId().getTurbineId()
                            ))
                            .availabilityType(availabilityData.getAvailabilityType())
                            .time(availabilityData.getTime())
                            .createdAt(LocalDateTime.now())
                            .build());

                    availabilityData.setTime(availability.getTime());
                    availabilityData.setUpdatedAt(LocalDateTime.now());
                    availabilityData.setUpdatedBy(principal.getName());
                    availabilityData.setChanged(true);

                }
                //기존 저장되어 있는 데이터가 없을때
                else{
                    Optional<AvailabilityType> optionalAvailabilityType  = availabilityTypeList.stream()
                            .filter(data -> data.getName().equalsIgnoreCase(availability.getName()))
                            .findFirst();

                    if(optionalAvailabilityType.isPresent()){
                        AvailabilityType availabilityType = optionalAvailabilityType.get();

                        availabilityDataList.add(
                                AvailabilityData.builder()
                                        .availabilityDataId(new AvailabilityData.AvailabilityDataId(turbine.getTimestamp().plusHours(9), 0, turbine.getTurbineId(), UUID.randomUUID()))
                                        .availabilityType(availabilityType)
                                        .time(availability.getTime())
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .updatedBy(principal.getName())
                                        .changed(true)
                                        .build()
                        );
                    }
                }
            }

            availabilityDataRepository.saveAll(availabilityDataList);
        }
    }



    @Override
    public AnnuallyWindFarmDTO.Response getAnnuallyWindFarmGeneralInfo(LocalDateTime searchTime) throws Exception {
        List<Parameter> parameters = parameterRepository.findAll();

        Parameter parameter = parameters.get(0);

        Duration duration = Duration.between(parameter.getWarrantyDate(), searchTime);

        int howManyYearsOfWarranty = (int)(duration.toDays() / 365) + 1;

        LocalDateTime startTimeOfWarranty = parameter.getWarrantyDate().plusYears((duration.toDays() / 365));

        int turbinesNumber = windFarmProperties.getTurbinesNumber();
        List<Map<LocalDateTime, Avail>> turbineAvailTotal = getMap(startTimeOfWarranty);

        //set turbines
        List<AnnuallyWindFarmDTO.Response.Turbine> turbines = new ArrayList<>();

        double windFarmNumerator = 0; //분자
        double windFarmDenominator = 0; //분모
        double windFarmEtcTime = 0;

        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i++) {
            double numerator = 0; //분자
            double denominator = 0; //분모
            double etcTime = 0;

            Map<LocalDateTime, Avail> availMap = turbineAvailTotal.get(i);

            //set data
            List<AnnuallyWindFarmDTO.Response.Data> dataList = new ArrayList<>();
            LocalDateTime endTime = LocalDateTime.now().isBefore(startTimeOfWarranty.plusYears(1)) ? LocalDateTime.now() : startTimeOfWarranty.plusYears(1);

            for(LocalDateTime time = startTimeOfWarranty; time.isBefore(endTime); time = time.plusDays(1)){
                double dailyTurbineAvailability = 0;

                if(availMap.containsKey(time)){
                    Avail avail = availMap.get(time);

                    numerator += avail.getNumerator();
                    denominator += avail.getDenominator();
                    etcTime += avail.getEtcTime();

                    windFarmNumerator += avail.getNumerator();
                    windFarmDenominator += avail.getDenominator();
                    windFarmEtcTime += avail.getEtcTime();

                    dailyTurbineAvailability = avail.getAvail();
                }

                dataList.add(new AnnuallyWindFarmDTO.Response.Data(time,  dailyTurbineAvailability));
            }

            double turbineAvailability;

            try {
                turbineAvailability = Avail.calcFormula(numerator, denominator, etcTime);
            }catch (Exception e){
                turbineAvailability = 0;
            }

            turbines.add(new AnnuallyWindFarmDTO.Response.Turbine(i + 1, turbineAvailability, dataList));
        }

        double windFarmAvailability = Avail.calcFormula(windFarmNumerator, windFarmDenominator, windFarmEtcTime);


        //calc capacity factor
        double capacityFactor = getCapacityFactor(startTimeOfWarranty);


        return AnnuallyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .availability(windFarmAvailability)
                .capacityFactor(capacityFactor)
                .yearsOfWarranty(howManyYearsOfWarranty)
                .startTimeOfYears(startTimeOfWarranty)
                .turbines(turbines)
                .date(searchTime)
                .build();
    }

    private double getCapacityFactor(LocalDateTime startTime) throws Exception {
        List<General> generalList = generalRepository.findAll();

        double bottom = 0;

        LocalDateTime endTime = getMaximumTime(startTime, LocalDateTime.now());

        Duration duration = Duration.between(startTime, endTime);
        long period = duration.toSeconds();

        double actualActivePower = 0;
        for(General general : generalList)
        {
            bottom += (general.getRatedPower() * ((double) period / 3600));
            Optional<Double> endTimeTotalPower = availabilityDataRepository.getTimeBeforeCertainTimestamp(general.getGeneralId().getTurbineId(), ConstantVariable.TOTAL_PRODUCTION_POWER.getUuid(), endTime, startTime);
            Optional<Double> startTimeTotalPower = availabilityDataRepository.getTimeAfterCertainTimestamp(general.getGeneralId().getTurbineId(), ConstantVariable.TOTAL_PRODUCTION_POWER.getUuid(), startTime, endTime);

            if(endTimeTotalPower.isEmpty() || startTimeTotalPower.isEmpty())
                actualActivePower += 0;
            else
                actualActivePower += endTimeTotalPower.get() - startTimeTotalPower.get();
        }
        return actualActivePower / bottom * 100;
    }

    private LocalDateTime getMaximumTime(LocalDateTime time, LocalDateTime max){
        LocalDateTime result;

        if(time.plusYears(1).isBefore(max)){
            result = time.plusYears(1);
        }
        else result = max;

        return result;
    }
    @Override
    public RealTimeDTO.Response getRealTimeData() {
        List<RealTimeDTO.Response.RealTime> dataList = new ArrayList<>();

        for(Turbine turbine : windFarm.getTurbines()){
            Map<String, Turbine.Data> turbineDataMap = turbine.getDataMap();

            for(String valueName : turbineDataMap.keySet())
            {
                Optional<RealTimeDTO.Response.RealTime> optionalRealTime = dataList.stream().filter(data -> data.getName().equalsIgnoreCase(valueName)).findFirst();

                if(optionalRealTime.isEmpty()) {
                    dataList.add(RealTimeDTO.Response.RealTime.builder()
                            .name(valueName)
                            .value(turbineDataMap.get(valueName).getValue())
                            .base(1)
                            .build());
                }else {
                    RealTimeDTO.Response.RealTime realTime = optionalRealTime.get();
                    realTime.setValue(realTime.getValue() + turbineDataMap.get(valueName).getValue());
                    realTime.setBase(realTime.getBase() + 1);
                }
            }
        }

        return RealTimeDTO.Response.builder()
                .timestamp(LocalDateTime.now())
                .dataList(dataList)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Avail{
        private double numerator = 0; //분자
        private double denominator = 0; //분모
        private double etcTime = 0;

        public static double average(List<Avail> availabilityList){
            Avail result = new Avail();

            for(Avail availability : availabilityList){
                result.setNumerator(result.getNumerator() + availability.getNumerator());
                result.setDenominator(result.getDenominator() + availability.getDenominator());
                result.setEtcTime(result.getEtcTime() + availability.getEtcTime());
            }

            return result.getAvail();
        }
        public static double calcFormula(double numerator, double denominator, double etcTime){
            if((etcTime - denominator) == 0)
                return 100;
            return (1 - (numerator / (etcTime - denominator))) * 100;
        }

        public static Avail returnZero(){
            return new Avail(1, 1, 2);
        }
        public double getAvail(){
            return Avail.calcFormula(numerator, denominator, etcTime);
        }

        public void calcAvailability(AvailabilityData availabilityData){

            if(availabilityData.getAvailabilityType() != null) {
                if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.NORMAL_TYPE)) {
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.FORCED_OUTAGE_TYPE)) {
                    numerator += availabilityData.getTime();
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.REQUESTED_SHUTDOWN_TYPE)) {
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.SCHEDULED_MAINTENANCE_TYPE)) {
                    denominator += availabilityData.getTime();
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE)) {
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.LOW_TEMPERATURE_TYPE)) {
                    etcTime += availabilityData.getTime();
                }
                else if (availabilityData.getAvailabilityType().getUuid().toString().equalsIgnoreCase(AvailabilityStatus.ETC_TYPE)) {
                    etcTime += availabilityData.getTime();
                }
            }
        }

        public void calcAvailability(String uuid, Double time){

            if (uuid.equalsIgnoreCase(AvailabilityStatus.NORMAL_TYPE)) {
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.FORCED_OUTAGE_TYPE)) {
                numerator += time;
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.REQUESTED_SHUTDOWN_TYPE)) {
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.SCHEDULED_MAINTENANCE_TYPE)) {
                denominator += time;
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE)) {
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.LOW_TEMPERATURE_TYPE)) {
                etcTime += time;
            }
            else if (uuid.equalsIgnoreCase(AvailabilityStatus.ETC_TYPE)) {
                etcTime += time;
            }
        }

        public double getAvailability(List<AvailabilityData> availabilityDataList){
            for(AvailabilityData availabilityData : availabilityDataList){
                calcAvailability(availabilityData);

            }
            return getAvail();
        }

        public double getAvailability(Map<String, RowData.DataSet> availabilityDataList){
            for(String uuid : availabilityDataList.keySet()){
                calcAvailability(uuid, availabilityDataList.get(uuid).getValue());
            }
            return getAvail();
        }
    }

    private List<Map<LocalDateTime, Avail>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusYears(1));
        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, Avail>> turbines = new ArrayList<>();

        for(int turbineId = 0; turbineId < windFarmProperties.getTurbinesNumber(); turbineId++)
        {
            Map<LocalDateTime, Avail> data = new HashMap<>();
            if(listMap.containsKey(turbineId)) {
                List<AvailabilityData> availabilityDataList1 = listMap.get(turbineId);

                int index= 0;
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

    public Map<LocalDateTime, Map<Integer, Double>> getCapacityFactor2(List<AvailabilityData> availabilityDataList){
        Map<LocalDateTime, Map<Integer, Double>> result = new LinkedHashMap<>();

        Map<LocalDateTime, Map<Integer, List<AvailabilityData>>> availabilityDataMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getAvailabilityDataId().getTimestamp(),
                        LinkedHashMap::new,
                        Collectors.groupingBy(data -> data.getAvailabilityDataId().getTurbineId())));

        for(LocalDateTime time : availabilityDataMap.keySet()){
            Map<Integer, Double> availabilityMap = new LinkedHashMap<>();

            for(Integer turbineId : availabilityDataMap.get(time).keySet())
            {
                for(AvailabilityData availabilityData : availabilityDataMap.get(time).get(turbineId)){
                    if(availabilityData.getVariable() != null && availabilityData.getVariable().getUuid().toString().equalsIgnoreCase(ConstantVariable.TOTAL_PRODUCTION_POWER.getUuid().toString()))
                        availabilityMap.put(turbineId, availabilityData.getTime().doubleValue());
                }
            }
            result.put(time, availabilityMap);

        }
        return result;
    }
    public Map<LocalDateTime, Map<Integer, Object>> test(List<AvailabilityData> availabilityDataList, String uuid) {

        Map<LocalDateTime, Map<Integer, Object>> result = new LinkedHashMap<>();

        Map<LocalDateTime, Map<Integer, List<AvailabilityData>>> availabilityDataMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getAvailabilityDataId().getTimestamp(),
                        LinkedHashMap::new,
                        Collectors.groupingBy(data -> data.getAvailabilityDataId().getTurbineId())));

        Avail a = new Avail();

        for(LocalDateTime time : availabilityDataMap.keySet()){
            Map<Integer, Object> availabilityMap = new LinkedHashMap<>();

            for(Integer turbineId : availabilityDataMap.get(time).keySet())
            {
                double availability = a.getAvailability(availabilityDataMap.get(time).get(turbineId));
                availabilityMap.put(turbineId, availability);
            }
            result.put(time, availabilityMap);
        }

        return result;
    }
    public Map<LocalDateTime, Map<Integer, Double>> getAvailability(List<AvailabilityData> availabilityDataList){
        Map<LocalDateTime, Map<Integer, Double>> result = new LinkedHashMap<>();

        Map<LocalDateTime, Map<Integer, List<AvailabilityData>>> availabilityDataMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getAvailabilityDataId().getTimestamp(),
                        LinkedHashMap::new,
                        Collectors.groupingBy(data -> data.getAvailabilityDataId().getTurbineId())));

        Avail a = new Avail();

        for(LocalDateTime time : availabilityDataMap.keySet()){
            Map<Integer, Double> availabilityMap = new LinkedHashMap<>();

            for(Integer turbineId : availabilityDataMap.get(time).keySet())
            {
                double availability = a.getAvailability(availabilityDataMap.get(time).get(turbineId));
                availabilityMap.put(turbineId, availability);
            }
            result.put(time, availabilityMap);
        }

        RowData test = new RowData();
        test.setData(availabilityDataList);
        test.getAvailability();
        return result;
    }
}
