package com.unison.scada.availability.scheduler.availability;

import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AvailabilityTotalData {
    private final List<Map<String, Integer>> availabilityStatusMaps = new ArrayList<Map<String, Integer>>();
    private final WindFarmProperties windFarmProperties;
    private int elapsedTotalTime;

    AvailabilityTotalData(WindFarmProperties windFarmProperties){
        this.windFarmProperties = windFarmProperties;
        this.elapsedTotalTime = 0;
        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i ++)
        {
            availabilityStatusMaps.add(new HashMap<String, Integer>());
        }

        for(Map<String, Integer> availabilityStatusMap : availabilityStatusMaps){

            for (AvailabilityStatus availabilityStatus : AvailabilityStatus.values()){
                String groupName = availabilityStatus.getGroupName();

                if(!groupName.equalsIgnoreCase(AvailabilityStatus.NONE_STATUS)
                        && !availabilityStatusMap.containsKey(groupName))
                {
                    availabilityStatusMap.put(groupName, 0);
                }
            }
        }
    }

    public void setAvailabilityTime(int turbineId, String groupName, int time) throws Exception {
        Map<String, Integer> availabilityStatusMap = availabilityStatusMaps.get(turbineId - 1);

        checkContainsKey(availabilityStatusMap, groupName);
        availabilityStatusMap.put(groupName, time);
    }

    public int getAvailabilityTime(int turbineId, String groupName) throws Exception {
        Map<String, Integer> availabilityStatusMap = availabilityStatusMaps.get(turbineId - 1);

        checkContainsKey(availabilityStatusMap, groupName);
        return availabilityStatusMap.get(groupName);
    }

    public void cleanAllTime(int turbinesNumber){
        for(int i = 0; i < turbinesNumber; i++){
            availabilityStatusMaps.get(i).replaceAll((k, v) -> 0);
        }
        elapsedTotalTime = 0;
    }

    private void checkContainsKey(Map<String, Integer> availabilityStatusMap, String key) throws Exception {
        if(!availabilityStatusMap.containsKey(key)){
            throw new Exception("not allowed group name of availability");
        }
    }

    public Map<String, Integer> getTotalTimeByTurbineId(int turbineId) {

        return availabilityStatusMaps.get(turbineId - 1);
    }

    public Map<String, Integer> getAHourAvailabilityTimeByTurbineId(int turbineId){

        Map<String, Integer> result = new HashMap<>();

        Map<String, Integer> AvailabilityTotalTime = availabilityStatusMaps.get(turbineId - 1);

        for(String availabilityName : AvailabilityTotalTime.keySet())
        {
            int totalTimeByName = AvailabilityTotalTime.get(availabilityName);

            if(availabilityName.equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS))
            {
                result.put(availabilityName, totalTimeByName + elapsedTotalTime);
            }
            else{
                result.put(availabilityName, totalTimeByName);
            }
        }
        return result;
    }
}
