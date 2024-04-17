package com.unison.scada.availability.comm.opcda;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum OPCGroupName {
    REAL_TIME("RealTime"),
    AVAILABILITY("Availability");

    private final String name;

    OPCGroupName(String name){
        this.name = name;
    }

    public static List<OPCGroupName> getGroupsNames(){

        return new ArrayList<>(Arrays.asList(OPCGroupName.values()));
    }
}
