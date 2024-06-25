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
                        data -> DateTimeUtil.formatToYearMonthDayHour(data.getAvailabilityDataId().getTimestamp()),
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


    public void clacValue(String reportType){
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

        Iterator<String> iterator = modifiedDataMap.keySet().iterator();
        String beforeTime = null;

        if (iterator.hasNext())
            beforeTime = iterator.next();

        while (iterator.hasNext()) {
            String time = iterator.next();
            Map<Integer, Map<String, DataSet>> maps = modifiedDataMap.get(time);

            Map<Integer, Double> result12 = new LinkedHashMap<>();

            /*
             * Set Turbine data
             * */
            for (Integer turbineId : maps.keySet()) {
                WindFarmService.Avail avail = new WindFarmService.Avail();

                Map<String, DataSet> mapss = maps.get(turbineId);
                if (mapss.containsKey(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid())
                        && modifiedDataMap.get(beforeTime).containsKey(turbineId)
                        && modifiedDataMap.get(beforeTime).get(turbineId).containsKey(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid())) {
                    double v = mapss.get(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid()).getValue()
                            - modifiedDataMap.get(beforeTime).get(turbineId).get(ConstantVariable.TOTAL_PRODUCTION_POWER.getStringUuid()).getValue();
                    result12.put(turbineId, v);
                }
            }

            if (!result12.isEmpty())
                result.put(beforeTime, result12);

            beforeTime = time;
        }
        return result;
    }

    private Map<String, Map<Integer, Double>> getCapacityFactorMap(){
        Map<String, Map<Integer, Double>> result = new LinkedHashMap<>();

        for(String localDateTime : energyProductionMap.keySet()){
            Map<Integer, Double> turbines = new LinkedHashMap<>();

            String nextKey = (String)getNextKey(energyProductionMap, localDateTime);

                for(Integer turbineId : energyProductionMap.get(localDateTime).keySet()){
                    Double energyProduction = energyProductionMap.get(localDateTime).get(turbineId);

                    double period;
                    if(nextKey != null)
                    {
                        Duration duration = Duration.between(
                                Objects.requireNonNull(DateTimeUtil.parseLocalDateTimeSeconds(localDateTime))
                                , DateTimeUtil.parseLocalDateTimeSeconds(nextKey));

                        period = duration.toSeconds();
                    }else{  // Last time
                        period = 3600;

                    }
                    turbines.put(turbineId, TurbineCalcUtil.getCapacityFactor(energyProduction, 2.3, period));
                }

            result.put(localDateTime, turbines);
        }
        return result;
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

    private static Object getNextKey(Map<Object, Object> map, String key) {
        List<Object> keys = new ArrayList<>(map.keySet());
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
    @NoArgsConstructor
    public static class DataSet{
        Double value = null;
        Double baseValue = null;
        RowDataMethod method;
        int baseIndex;


        public DataSet(double value){
            this(value, null, RowDataMethod.POINT, 1);
        }

        public DataSet(double value, RowDataMethod method){
            this(value, null, method, 1);
        }

        public Double convertValue(){
            if(method == RowDataMethod.POINT) {
                return value;
            }
            else if(method == RowDataMethod.AVERAGE){
                return value / baseIndex;
            }
            else if(method == RowDataMethod.GROWTH){
                return value - baseValue;
            }else
                return value;
        }

        public void setValue(double value){
            if(method == RowDataMethod.POINT && this.value == null) {
                this.value = value;
            }
            else if(method == RowDataMethod.AVERAGE){
                this.value += value;
                this.baseIndex++;
            }
            else if(method == RowDataMethod.SUM){
                this.value += value;
            }
            else if(method == RowDataMethod.GROWTH){
                if(baseValue == null)
                    this.baseValue = value;
            }
        }
    }
}
