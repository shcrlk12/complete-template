package com.unison.scada.availability.scheduler.availability;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.comm.opcda.*;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TurbineDataUpdateByOPCService implements TurbineDataUpdateService {
    Logger logger = LoggerFactory.getLogger(TurbineDataUpdateByOPCService.class);

    private final int DEFAULT_OPC_GROUP_UPDATE_RATE;

    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final AvailabilityTotalData availabilityTotalData;
    private final WindFarmProperties windFarmProperties;
    private final OPCProperties opcProperties;
    private OPCServer opcServer;

    private boolean initialize = false;

    private final String prefix;

    TurbineDataUpdateByOPCService(AvailabilityDataRepository availabilityDataRepository, AvailabilityTypeRepository availabilityTypeRepository,AvailabilityTotalData availabilityTotalDatas, WindFarmProperties windFarmProperties, OPCProperties opcProperties){
        this.availabilityTypeRepository = availabilityTypeRepository;
        this.availabilityDataRepository = availabilityDataRepository;
        this.availabilityTotalData = availabilityTotalDatas;
        this.windFarmProperties = windFarmProperties;
        this.opcProperties = opcProperties;

        prefix = opcProperties.getPrefixFormat();
        DEFAULT_OPC_GROUP_UPDATE_RATE = 500;
    }

    private void initialize() throws OPCException, OPCNotFoundException {

        this.opcServer = new OPCServer(opcProperties.getServerName());
        List<String> prefixNames = createOPCPrefixNames();

        for (String prefixName : prefixNames) {
            for (OPCGroupName opcGroupName : OPCGroupName.getGroupsNames()) {

                opcServer.addNewGroup(prefixName + opcGroupName.getName(), (int) (long) DEFAULT_OPC_GROUP_UPDATE_RATE, 0, true);

                for (OPCVariable opcVariable : OPCVariable.getVariablesByName(opcGroupName)) {
                    addOPCItem(prefixName + opcGroupName.getName(), prefixName + opcVariable.getVariableName());
                }
            }
        }
    }

    private List<String> createOPCPrefixNames() throws OPCException {
        List<String> result = new ArrayList<>();

        if(windFarmProperties.getTurbinesNumber() == 0){
            throw new OPCException("Turbine number not allow 0");
        }

        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i++)
        {
            result.add(getPrefixByTurbineId(i + 1));
        }

        return result;
    }

    private String getPrefixByTurbineId(int turbineId) throws OPCException {
        String result;

        try{
            result = String.format(prefix, turbineId);
        }catch (Exception e){
            throw new OPCException("Not match prefix[" + prefix + "] with number[" + turbineId + "]");
        }

        return result;
    }

    private void toPrint(){

        Map<String, OPCGroup> map = opcServer.getAllOPCGroups();

        for(String key : map.keySet())
        {
            OPCGroup opcGroup = map.get(key);
            List<OPCItem> opcItems = opcGroup.getAllItems();

            for(OPCItem opcItem : opcItems)
            {
                System.out.println(opcItem.getName() + opcItem.getID() + opcItem.getVtType() + opcItem.getValueAsString());
            }
        }
    }

    private Turbine getTurbineFromOPC(int turbineId) throws OPCNotFoundException, OPCException {
        String prefixTurbineId = getPrefixByTurbineId(turbineId);

        //get variable of real time opc group
        OPCGroup realTimeOPCGroup = opcServer.getGroupByName(prefixTurbineId + OPCGroupName.REAL_TIME.getName());
        realTimeOPCGroup.syncRead(OPC.OPC_DS_CACHE);

        OPCItem activePowerOPCItem = realTimeOPCGroup.getItemByName(prefixTurbineId + OPCVariable.ACTIVE_POWER.getVariableName());
        OPCItem windSpeedOPCItem = realTimeOPCGroup.getItemByName(prefixTurbineId + OPCVariable.WIND_SPEED.getVariableName());

        String activePower = activePowerOPCItem.getValueAsString();
        String windSpeed = windSpeedOPCItem.getValueAsString();

        //get variable of availability opc group
        OPCGroup availabilityOPCGroup = opcServer.getGroupByName(prefixTurbineId + OPCGroupName.AVAILABILITY.getName());
        availabilityOPCGroup.syncRead(OPC.OPC_DS_CACHE);

        OPCItem availabilityOPCItem = availabilityOPCGroup.getItemByName(prefixTurbineId + OPCVariable.AVAILABILITY.getVariableName());
        String availability = availabilityOPCItem.getValueAsString();

        return Turbine.builder()
                .turbineId(turbineId)
                .activePower(Double.parseDouble(activePower))
                .windSpeed(Double.parseDouble(windSpeed))
                .availabilityStatus(AvailabilityStatus.getStatus(Integer.parseInt(availability)))
                .build();
    }
    private void addOPCItem(String groupName, String itemName) throws OPCNotFoundException, OPCException {
        logger.info("addOPCItem[" + itemName + "].");

        OPCGroup opcGroup = opcServer.getGroupByName(groupName);
        opcGroup.addNewItem(itemName, itemName, true, "-");
    }


    @Override
    public Turbine getUpdatedTurbineData(int turbineId) throws OPCException, OPCNotFoundException {
        if(!initialize) {
            initialize();
            initialize = true;
        }

        return getTurbineFromOPC(turbineId);
    }

    @Override
    public void saveAHourAvailability(int turbinesNumber) {
        List<AvailabilityData> totalTimes = new ArrayList<>();

        List<AvailabilityType> availabilityTypes = availabilityTypeRepository.findByActive(true);

        for(int i = 1; i <= turbinesNumber; i++){

            Map<String, Integer> totalTime = availabilityTotalData.getTotalTimeByTurbineId(i);

            for(String key : totalTime.keySet()){
                int time = totalTime.get(key);
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime oneHourAgo = currentDateTime.minusHours(1).withMinute(0).withSecond(0).withNano(0);

                totalTimes.add(AvailabilityData.builder()
                                .availabilityDataId(new AvailabilityData.AvailabilityDataId(oneHourAgo, i, UUID.randomUUID()))
                                .availabilityType(findAvailabilityTypeByName(key, availabilityTypes))
                                .time(time)
                                .createdAt(LocalDateTime.now())
                        .build());
            }
        }
        availabilityDataRepository.saveAll(totalTimes);

        availabilityTotalData.cleanAllTime(turbinesNumber);
    }

    private List<AvailabilityData> test1(List<AvailabilityType> availabilityTypes){

    }
    private AvailabilityType findAvailabilityTypeByName(String name, List<AvailabilityType> availabilityTypes){
        for(AvailabilityType availabilityType : availabilityTypes) {
            if(availabilityType.getName().equalsIgnoreCase(name))
                return availabilityType;
        }
        return null;
    }
    @Override
    public void updateTotalAvailability(List<Turbine> turbines) throws Exception {
        for (Turbine turbine : turbines){

            int turbineId = turbine.getTurbineId();
            AvailabilityStatus availabilityStatus = turbine.getAvailabilityStatus();

            if(availabilityStatus.getGroupName().equalsIgnoreCase(AvailabilityStatus.NORMAL_STATUS))
            {
                int time = availabilityTotalData.getAvailabilityTime(turbineId, AvailabilityStatus.NORMAL_STATUS);
                availabilityTotalData.setAvailabilityTime(turbineId, AvailabilityStatus.NORMAL_STATUS, time + 2);
            }
            else if(availabilityStatus.getGroupName().equalsIgnoreCase(AvailabilityStatus.FORCED_OUTAGE_STATUS))
            {
                int time = availabilityTotalData.getAvailabilityTime(turbineId, AvailabilityStatus.FORCED_OUTAGE_STATUS);
                availabilityTotalData.setAvailabilityTime(turbineId, AvailabilityStatus.FORCED_OUTAGE_STATUS, time + 2);
            }
            else if(availabilityStatus.getGroupName().equalsIgnoreCase(AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS))
            {
                int time = availabilityTotalData.getAvailabilityTime(turbineId, AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS);
                availabilityTotalData.setAvailabilityTime(turbineId, AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS, time + 2);
            }
            else if(availabilityStatus.getGroupName().equalsIgnoreCase(AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS))
            {
                int time = availabilityTotalData.getAvailabilityTime(turbineId, AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS);
                availabilityTotalData.setAvailabilityTime(turbineId, AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS, time + 2);
            }
            else
            {
                int time = availabilityTotalData.getAvailabilityTime(turbineId, AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS);
                availabilityTotalData.setAvailabilityTime(turbineId, AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS, time + 2);
            }
        }
    }
}
