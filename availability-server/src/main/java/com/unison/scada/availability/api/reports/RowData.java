package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.variable.ConstantVariable;
import com.unison.scada.availability.api.windfarm.WindFarmService;
import com.unison.scada.availability.global.DateTimeUtil;
import com.unison.scada.availability.global.NullUtil;
import com.unison.scada.availability.global.TurbineCalcUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RowData {
    private Map<String, Map<Integer, Map<String, DataSet>>> rowDataMap = new LinkedHashMap<>();

    @Getter
    private Map<String, Map<Integer, Map<String, DataSet>>> modifiedDataMap = new LinkedHashMap<>();

    private Map<String, Map<Integer, Double>> availabilityMap = new LinkedHashMap<>();
    private Map<String, Map<Integer, Double>> energyProductionMap = new LinkedHashMap<>();
    private Map<String, Map<Integer, Double>> capacityFactorMap = new LinkedHashMap<>();

    private String reportType;

    public void setData(List<AvailabilityData> availabilityDataList) {
        Map<String, Map<Integer, List<AvailabilityData>>> availabilityDataMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy(
                        data -> DateTimeUtil.formatToYearMonthDayHourMinute(data.getAvailabilityDataId().getTimestamp()),
                        LinkedHashMap::new,
                        Collectors.groupingBy(data -> data.getAvailabilityDataId().getTurbineId())));

        for (String time : availabilityDataMap.keySet()) {
            Map<Integer, Map<String, DataSet>> turbineMap = new LinkedHashMap<>();

            for (Integer turbineId : availabilityDataMap.get(time).keySet()) {
                Map<String, DataSet> dataMap = new LinkedHashMap<>();

                for (AvailabilityData availabilityData : availabilityDataMap.get(time).get(turbineId)) {
                    if (NullUtil.isNotNull(availabilityData.getAvailabilityType())) {
                        String uuid = availabilityData.getAvailabilityType().getUuid().toString();

                        dataMap.put(uuid, new DataSet(availabilityData.getTime(), RowDataMethod.SUM));
                    } else if (NullUtil.isNotNull(availabilityData.getVariable())) {
                        String uuid = availabilityData.getVariable().getUuid().toString();

                        if(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid().equalsIgnoreCase(uuid)){
                            dataMap.put(uuid, new DataSet(availabilityData.getTime(), RowDataMethod.GROWTH));
                        }else if(ConstantVariable.WIND_SPEED.getStringUuid().equalsIgnoreCase(uuid)){
                            dataMap.put(uuid, new DataSet(availabilityData.getTime(), RowDataMethod.AVERAGE));
                        }
                    }
                }
                turbineMap.put(turbineId, dataMap);
            }
            rowDataMap.put(time, turbineMap);
        }
    }


    public void clacValue(String reportType) throws Exception {
        this.reportType = reportType;

        if(reportType.equalsIgnoreCase("Hourly")){
            modifiedDataMap = rowDataMap;
        }else if(reportType.equalsIgnoreCase("Daily")){
            modifiedDataMap = groupingDaily("Daily");

        }else if(reportType.equalsIgnoreCase("Weekly")){
            modifiedDataMap = groupingDaily("Weekly");

        }else if(reportType.equalsIgnoreCase("Monthly")){
            modifiedDataMap = groupingDaily("Monthly");

        }else if(reportType.equalsIgnoreCase("Quarter")){
            modifiedDataMap = groupingDaily("Quarter");

        }else if(reportType.equalsIgnoreCase("Annually")){
            modifiedDataMap = groupingDaily("Annually");

        }
        /*
         * Set availability map
         * */

        availabilityMap = getAvailability();
        /*
         * Set energy production map
         * */

        energyProductionMap = getEnergyProduction();

        /*
         * Set capacity factor map
         * */

        capacityFactorMap = getCapacityFactorMap();
    }

    public Map<String, Map<Integer, Double>> getAvailability() {
        Map<String, Map<Integer, Double>> result = new LinkedHashMap<>();

        for (String time : modifiedDataMap.keySet()) {
            Map<Integer, Map<String, DataSet>> maps = modifiedDataMap.get(time);

            Map<Integer, Double> result12 = new LinkedHashMap<>();

            for (Integer turbineId : maps.keySet()) {
                WindFarmService.Avail avail = new WindFarmService.Avail();

                Map<String, DataSet> mapss = maps.get(turbineId);
                Double availability = avail.getAvailability(mapss);
                result12.put(turbineId, availability);
            }
            result.put(time, result12);
        }

        return result;
    }

    public Map<String, Map<Integer, Double>> getEnergyProduction() {
        Map<String, Map<Integer, Double>> result = new LinkedHashMap<>();

        for(String localDateTime : modifiedDataMap.keySet()) {
            String nextKey = getNextKey(modifiedDataMap, localDateTime);

            Map<Integer, Double> energyProductionMap = new LinkedHashMap<>();
            Map<Integer, Map<String, DataSet>> maps = modifiedDataMap.get(localDateTime);

            /*
             * Set Turbine data
             * */
            for (Integer turbineId : maps.keySet()) {

                Map<String, DataSet> uuidMap = maps.get(turbineId);
                double energyProduction;
                if (uuidMap.containsKey(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid())
                        && nextKey != null
                        && modifiedDataMap.get(nextKey).containsKey(turbineId)
                        && modifiedDataMap.get(nextKey).get(turbineId).containsKey(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid())) {
                    energyProduction = modifiedDataMap.get(nextKey).get(turbineId).get(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid()).getValue()
                                        - uuidMap.get(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid()).getValue();
                }
                else if(uuidMap.containsKey(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid())){
                    energyProduction = uuidMap.get(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid()).convertValue();
                }
                else {
                    energyProduction = 0;
                }
                energyProductionMap.put(turbineId, energyProduction);
            }
            result.put(localDateTime, energyProductionMap);
        }

        return result;
    }

    private Map<String, Map<Integer, Double>> getCapacityFactorMap() throws Exception {
        Map<String, Map<Integer, Double>> result = new LinkedHashMap<>();

        for(String localDateTime : energyProductionMap.keySet()){
            Map<Integer, Double> turbines = new LinkedHashMap<>();

            for(Integer turbineId : energyProductionMap.get(localDateTime).keySet()){
                Double energyProduction = energyProductionMap.get(localDateTime).get(turbineId);

                double period = getPeriod(localDateTime);

                turbines.put(turbineId, TurbineCalcUtil.getCapacityFactor(energyProduction, 2.3, period));
            }

            result.put(localDateTime, turbines);
        }
        return result;
    }

    private static <K, V> K getLastKey(Map<K, V> map) {
        K lastKey = null;
        for (K key : map.keySet()) {
            lastKey = key;
        }
        return lastKey;
    }

    private double getPeriod(String time) throws Exception {

        String lastKey = getLastKey(rowDataMap);
        LocalDateTime lastTime = DateTimeUtil.parseLocalDateTimeSeconds(lastKey);
        LocalDateTime localDateTime = null;
        LocalDateTime endTime = null;

        double period = 0d;

        if(reportType.equalsIgnoreCase("Hourly"))
        {
            return 3600d;
        }
        else if(reportType.equalsIgnoreCase("Daily"))
        {
            localDateTime = Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(time));

            endTime = localDateTime.plusDays(1);

        }
        else if(reportType.equalsIgnoreCase("Weekly"))
        {
            localDateTime = Objects.requireNonNull(DateTimeUtil.getWeeklyTime(time));

            endTime = localDateTime.plusWeeks(1);
        }
        else if(reportType.equalsIgnoreCase("Monthly"))
        {
            localDateTime = Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(time));

            endTime = localDateTime.plusMonths(1);

        }
        else if(reportType.equalsIgnoreCase("Quarter"))
        {
            localDateTime = Objects.requireNonNull(DateTimeUtil.getQuarterTime(time));

            endTime = localDateTime.plusMonths(3);
        }
        else if(reportType.equalsIgnoreCase("Annually"))
        {
            localDateTime = Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(time));

            endTime = localDateTime.plusYears(1);
        }

        if(endTime == null)
        {
            throw new Exception("not correct report type [" + reportType + "]" );
        }

        if(endTime.isAfter(lastTime)){
            endTime = lastTime;
        }

        Duration duration = Duration.between(
                localDateTime
                , endTime);

        period = duration.toSeconds();

        return period;
    }
    public Double getEnergyProduction(String localDateTime, Integer turbineId) {
         return getMapData(energyProductionMap, localDateTime, turbineId);

    }

    public Double getAvailability(String localDateTime, Integer turbineId) {
        return getMapData(availabilityMap, localDateTime, turbineId);
    }

    public Double getCapacityFactorMap(String localDateTime, Integer turbineId) {
        return getMapData(capacityFactorMap, localDateTime, turbineId);
    }

    private Double getMapData(Map<String, Map<Integer, Double>> map, String localDateTime, Integer turbineId){
        if (map.containsKey(localDateTime)) {
            Map<Integer, Double> energyProductionByTurbine = map.get(localDateTime);

            if (energyProductionByTurbine.containsKey(turbineId))
                return energyProductionByTurbine.get(turbineId);
        }

        return 0d;
    }

    public Double getRowData(String uuid, String localDateTime, Integer turbineId){

        if (modifiedDataMap.containsKey(localDateTime)) {
            Map<Integer, Map<String, DataSet>> rowDataMapTurbine = modifiedDataMap.get(localDateTime);

            if (rowDataMapTurbine.containsKey(turbineId))
            {
                Map<String, DataSet> rowDataUuidMap = rowDataMapTurbine.get(turbineId);
                if(rowDataUuidMap.containsKey(uuid))
                {
                    return rowDataUuidMap.get(uuid).convertValue();
                }
            }
        }

        return 0d;
    }

    private static <K> K getNextKey(Map<K, ?> map, K key) {
        List<K> keys = new ArrayList<>(map.keySet());
        int index = keys.indexOf(key);

        if (index != -1 && index < keys.size() - 1) {
            return keys.get(index + 1);
        }
        return null; // 다음 키가 없으면 null 반환
    }

    private Map<String, Map<Integer, Map<String, DataSet>>> groupingDaily(String reportType){
        Map<String, Map<Integer, Map<String, DataSet>>> result = new LinkedHashMap<>();

        for(String localDateTime : rowDataMap.keySet()) {

            String dailyTime = localDateTime;

            if(reportType.equalsIgnoreCase("Daily")){
                dailyTime= DateTimeUtil.formatToYearMonthDay(Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime)));
            }else if(reportType.equalsIgnoreCase("Weekly")){
                dailyTime= DateTimeUtil.formatToYearWeekly(Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime)));
            }else if(reportType.equalsIgnoreCase("Monthly")){
                dailyTime= DateTimeUtil.formatToYearMonth(Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime)));
            }else if(reportType.equalsIgnoreCase("Quarter")){
                dailyTime= DateTimeUtil.formatToYearQuarter(Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime)));
            }else if(reportType.equalsIgnoreCase("Annually")){
                dailyTime= DateTimeUtil.formatToYear(Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime)));
            }

            for (Integer turbineId : rowDataMap.get(localDateTime).keySet()) {

                for (String uuid : rowDataMap.get(localDateTime).get(turbineId).keySet()) {
                    DataSet value = rowDataMap.get(localDateTime).get(turbineId).get(uuid);
                    test1234(result, dailyTime, turbineId, uuid, value);
                }
            }
        }
        return result;
    }


    private void test1234(Map<String, Map<Integer, Map<String, DataSet>>> map, String time, Integer turbineId, String uuid, DataSet value){
        Map<String, Map<Integer, Map<String, DataSet>>> result = new LinkedHashMap<>();

        if(map.containsKey(time))
        {
            Map<Integer, Map<String, DataSet>> getTurbineMap = map.get(time);
            if(getTurbineMap.containsKey(turbineId))
            {
                Map<String, DataSet> getUuidMap = getTurbineMap.get(turbineId);
                if(getUuidMap.containsKey(uuid))
                {
                    DataSet originValue = getUuidMap.get(uuid);

                    originValue.setValue(value.getValue());
                    getUuidMap.put(uuid, originValue);
                }
                else
                {
                    getUuidMap.put(uuid, value);

                    getUuidMap.putAll(map.get(time).get(turbineId));
                }
                getTurbineMap.put(turbineId, getUuidMap);
                result.put(time, getTurbineMap);
            }
            else
            {

                Map<String, DataSet> uuidValue = new LinkedHashMap<>();

                uuidValue.put(uuid, value);
                getTurbineMap.put(turbineId, uuidValue);
                result.put(time, getTurbineMap);
            }
        }
        else
        {
            Map<Integer, Map<String, DataSet>> turbineMap = new LinkedHashMap<>();
            Map<String, DataSet> uuidValue = new LinkedHashMap<>();

            uuidValue.put(uuid, value);
            turbineMap.put(turbineId, uuidValue);
            result.put(time, turbineMap);
        }

        map.putAll(result);
    }

    @Getter
    @AllArgsConstructor
    public static class DataSet{
        Double value;
        Double baseValue;
        RowDataMethod method;
        int baseIndex;

        public DataSet(RowDataMethod method){
            this(null, null, method, 0);
        }

        public DataSet(Double value, RowDataMethod method) {
            this(value, value, method, 1);

        }


        public Double convertValue(){
            if(method == RowDataMethod.POINT) {
                return value;
            }
            else if(method == RowDataMethod.AVERAGE){
                return value / baseIndex;
            }
            else if(method == RowDataMethod.GROWTH){
                if(baseIndex == 1)
                    return 0d;
                else
                    return value - baseValue;
            }else
                return value;
        }

        public void setValue(double value){
            this.baseIndex++;

            if(method == RowDataMethod.POINT && this.value == null) {
                this.value = value;
            }
            else if(method == RowDataMethod.AVERAGE){
                this.value += value;
            }
            else if(method == RowDataMethod.SUM){
                this.value += value;
            }
            else if(method == RowDataMethod.GROWTH){
                this.value = value;

                if(baseValue == null)
                    this.baseValue = value;
            }
        }
    }
}
