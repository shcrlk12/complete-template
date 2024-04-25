package com.unison.scada.availability.scheduler.availability.model;

import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AvailabilityTotalTime {
    private final int SCHEDULING_TIME_PERIOD = 2; // seconds
    private final List<Map<String, Integer>> availabilityTypeAndTimeMapOfAllTurbine = new ArrayList<Map<String, Integer>>();
    private int elapsedTotalTime;

    AvailabilityTotalTime(WindFarmProperties windFarmProperties){
        this.elapsedTotalTime = 0;

        // initialize map to type of availability status
        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i ++)
        {
            Map<String, Integer> availabilityTypeAndTimeMapOfATurbine = new HashMap<>();

            for (String type : AvailabilityStatus.getTypes()){
                availabilityTypeAndTimeMapOfATurbine.put(type, 0);
            }
            availabilityTypeAndTimeMapOfAllTurbine.add(availabilityTypeAndTimeMapOfATurbine);
        }
    }

    public void setAvailabilityTotalTime(Turbine turbine) throws Exception {
        int turbineId = turbine.getTurbineId();
        String availabilityType = turbine.getAvailabilityStatus().getType();

        int totalTime = getAvailabilityTimeByTurbineId(turbineId, availabilityType);

        setAvailabilityTimeByTurbineId(
                turbineId,
                availabilityType,
                totalTime + this.SCHEDULING_TIME_PERIOD
        );

        this.elapsedTotalTime += this.SCHEDULING_TIME_PERIOD;
    }

    public int getAvailabilityTimeByTurbineId(int turbineId, String type) throws Exception {
        Map<String, Integer> availabilityTypeAndTimeMapOfATurbine =
                availabilityTypeAndTimeMapOfAllTurbine.get(turbineId);

        checkContainsKey(availabilityTypeAndTimeMapOfATurbine, type);

        return availabilityTypeAndTimeMapOfATurbine.get(type);
    }

    public void setAvailabilityTimeByTurbineId(int turbineId, String type, int totalTime) throws Exception {
        Map<String, Integer> availabilityTypeAndTimeMapOfATurbine =
                availabilityTypeAndTimeMapOfAllTurbine.get(turbineId);

        checkContainsKey(availabilityTypeAndTimeMapOfATurbine, type);

        availabilityTypeAndTimeMapOfATurbine.put(type, totalTime);
    }

    public void cleanAllTotalTime(){

        for(Map<String, Integer> availabilityTypeAndTimeMapOfATurbine :
                availabilityTypeAndTimeMapOfAllTurbine)
        {
            availabilityTypeAndTimeMapOfATurbine.replaceAll((k, v) -> 0);
        }
        elapsedTotalTime = 0;
    }

    private void checkContainsKey(Map<String, Integer> availabilityStatusMap, String key) throws Exception {
        if(!availabilityStatusMap.containsKey(key)){
            throw new Exception("not allowed group name of availability");
        }
    }

    public Map<String, Integer> getTotalTimesByTurbineId(int turbineId) {

        return availabilityTypeAndTimeMapOfAllTurbine.get(turbineId);
    }

    public Map<String, Integer> get1HourAvailabilityTimeByTurbineId(int turbineId){
        Map<String, Integer> result = new HashMap<>();

        Map<String, Integer> availabilityTypeAndTimeMapOfATurbine =
                availabilityTypeAndTimeMapOfAllTurbine.get(turbineId);

        for(String availabilityType : availabilityTypeAndTimeMapOfATurbine.keySet())
        {
            int totalTime = availabilityTypeAndTimeMapOfATurbine.get(availabilityType);

            if(availabilityType.equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE))
            {
                result.put(availabilityType, totalTime + (elapsedTotalTime / availabilityTypeAndTimeMapOfAllTurbine.size()));
            }
            else{
                result.put(availabilityType, totalTime);
            }
        }
        return result;
    }
}
