package com.unison.scada.availability.comm.opcda;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum OPCVariable {
    WIND_SPEED(OPCGroupName.REAL_TIME, "U88_WNAC_sviWindSpeed1s"),
    ACTIVE_POWER(OPCGroupName.REAL_TIME, "U88_WGDC_sviTurW"),
    AVAILABILITY(OPCGroupName.AVAILABILITY, "U88_WTUR_sviTurOperationSt");

    private final String groupName;
    private final String variableName;

    OPCVariable(OPCGroupName groupName, String variableName){
        this.groupName = groupName.getName();
        this.variableName = variableName;

    }

    public static List<OPCVariable> getVariablesByName(OPCGroupName opcGroupName) throws OPCException {
        List<OPCVariable> result = new ArrayList<>();

        for(OPCVariable opcVariable : OPCVariable.values()){
            if(opcVariable.getGroupName().compareTo(opcGroupName.getName()) == 0){
                result.add(opcVariable);
            }
        }
        if(result.isEmpty())
            throw new OPCException("Not found variable of group by name");

        return result;
    }
}
