package com.unison.scada.availability.scheduler.availability.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public enum AvailabilityStatus{
    FULL_PERFORMANCE(10, AvailabilityStatus.NORMAL_TYPE),
    PARTIAL_PERFORMANCE(20, AvailabilityStatus.NORMAL_TYPE),
    TECHNICAL_STANDBY(30, AvailabilityStatus.NORMAL_TYPE),
    OUT_OF_ENVIRONMENTAL(40, AvailabilityStatus.NORMAL_TYPE),
    REQUESTED_SHUTDOWN(50, AvailabilityStatus.REQUESTED_SHUTDOWN_TYPE),
    OUT_OF_ELECTRICAL_SPECIFICATION(60, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE),
    SCHEDULED_MAINTENANCE(70, AvailabilityStatus.SCHEDULED_MAINTENANCE_TYPE),
    PLANNED_CORRECTIVE_ACTION(80, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE),
    FORCED_OUTAGE(90, AvailabilityStatus.FORCED_OUTAGE_TYPE),
    SUSPENDED(100, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE),
    FORCE_MAJEURE(110, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE),
    INFORMATION_UNAVAILABLE(120, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE);

    public static final String NORMAL_TYPE = "normal status";
    public static final String REQUESTED_SHUTDOWN_TYPE = "requested shutdown";
    public static final String FORCED_OUTAGE_TYPE = "forced outage";
    public static final String SCHEDULED_MAINTENANCE_TYPE = "scheduled maintenance";
    public static final String INFORMATION_UNAVAILABLE_TYPE = "information unavailable";

    @Getter
    private final int value;
    @Getter
    private final String type;

    AvailabilityStatus(int value) {
        this.value = value;
        this.type = null;
    }

    AvailabilityStatus(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public static AvailabilityStatus getStatus(int value){
        for(AvailabilityStatus availabilityStatus : AvailabilityStatus.values())
        {
            if(availabilityStatus.value == value){
                return availabilityStatus;
            }
        }
        return AvailabilityStatus.INFORMATION_UNAVAILABLE;
    }

    public static Set<String> getTypes(){
        Set<String> result = new HashSet<>();

        for(AvailabilityStatus availabilityStatus : AvailabilityStatus.values())
        {
            result.add(availabilityStatus.type);
        }
        return result;
    }
    public static List<Integer> getStatusCodes(String groupName){
        List<Integer> result = new ArrayList<>();

        for(AvailabilityStatus availabilityStatus : AvailabilityStatus.values())
        {
            if(availabilityStatus.type.compareTo(groupName) == 0){
                result.add(availabilityStatus.value);
            }
        }
        return result;
    }
}