package com.unison.scada.availability.scheduler.availability.update;

import com.unison.scada.availability.api.availability.variable.ConstantVariable;
import com.unison.scada.availability.api.availability.variable.Variable;
import com.unison.scada.availability.api.availability.variable.VariableRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.comm.opcda.*;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityTotalTime;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * OPC 통신을 사용하여 Turbine 데이터를 업데이트하는 서비스
 *
 * @version 1.0.0
 * @author Jeong Won Kim
 */
@Service
public class TurbineDataUpdateByOPCService implements TurbineDataUpdateService {
    Logger logger = LoggerFactory.getLogger(TurbineDataUpdateByOPCService.class);

    private final int DEFAULT_OPC_GROUP_UPDATE_RATE = 500;
    private final String opcGroupName = "WindTurbine";

    private final AvailabilityTotalTime availabilityTotalTime;
    private final WindFarmProperties windFarmProperties;
    private final OPCProperties opcProperties;
    private OPCServer opcServer;
    private final VariableRepository variableRepository;

    private boolean initialize = false;

    private final String prefix;

    TurbineDataUpdateByOPCService(AvailabilityTotalTime availabilityTotalDatas, WindFarmProperties windFarmProperties, OPCProperties opcProperties, VariableRepository variableRepository){
        this.availabilityTotalTime = availabilityTotalDatas;
        this.windFarmProperties = windFarmProperties;
        this.opcProperties = opcProperties;
        this.variableRepository = variableRepository;

        prefix = opcProperties.getPrefixFormat();
    }

    private void initialize() throws OPCException, OPCNotFoundException {
        /*
        * Initialize OPC Variables
        * */
        List<Variable> variableList = variableRepository.findByIsActiveTrue();
        ConstantVariable.setOpcVariableName(variableList);

        /*
        * Configure OPC Server
        * */
        this.opcServer = new OPCServer(opcProperties.getServerName());

        //create opc group names by prefix format
        List<String> prefixNames = createOPCPrefixNames();

        //config opc group name
        for (String prefixName : prefixNames) {
            opcServer.addNewGroup(prefixName + opcGroupName, (int) (long) DEFAULT_OPC_GROUP_UPDATE_RATE, 0, true);

            for (Variable variable : variableList) {
                addOPCItem(prefixName + opcGroupName, prefixName + variable.getName());
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
            result.add(getPrefixByTurbineId(i));
        }

        return result;
    }

    private String getPrefixByTurbineId(int turbineId) throws OPCException {
        String result;

        try{
            result = String.format(prefix, turbineId + 1);
        }catch (Exception e){
            throw new OPCException("Not match prefix[" + prefix + "] with number[" + turbineId + "]");
        }

        return result;
    }

    private Turbine updateTurbineFromOPC(int turbineId) throws OPCNotFoundException, OPCException {
        String prefixTurbineId = getPrefixByTurbineId(turbineId);

        //get variable of real time opc group
        OPCGroup realTimeOPCGroup = opcServer.getGroupByName(prefixTurbineId + opcGroupName);
        realTimeOPCGroup.syncRead(OPC.OPC_DS_CACHE);

        Map<String, Double> dataMap = new HashMap<>();

        for(ConstantVariable constantVariable : ConstantVariable.values()){
            String realTimeValue = realTimeOPCGroup.getItemByName(prefixTurbineId + constantVariable.getOpcVariableName()).getValueAsString();

            dataMap.put(constantVariable.getOpcVariableName(), Double.parseDouble(realTimeValue));
        }

        Double availabilitySt = dataMap.get(ConstantVariable.TURBINE_MAIN_STATUS.getOpcVariableName());

        return Turbine.builder()
                .turbineId(turbineId)
                .dataMap(dataMap)
                .availabilityStatus(AvailabilityStatus.getStatus(availabilitySt.intValue()))
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

        return updateTurbineFromOPC(turbineId);
    }



    @Override
    public void updateTotalAvailability(List<Turbine> turbines) throws Exception {
        for (Turbine turbine : turbines){
            availabilityTotalTime.setAvailabilityTotalTime(turbine);
        }
    }
}
