package com.unison.scada.availability.scheduler.availability.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public enum AvailabilityStatus{
    FULL_PERFORMANCE(10, AvailabilityStatus.NORMAL_STATUS),
    PARTIAL_PERFORMANCE(20, AvailabilityStatus.NORMAL_STATUS),
    TECHNICAL_STANDBY(30, AvailabilityStatus.NORMAL_STATUS),
    OUT_OF_ENVIRONMENTAL(40, AvailabilityStatus.NORMAL_STATUS),
    REQUESTED_SHUTDOWN(50, AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS),
    OUT_OF_ELECTRICAL_SPECIFICATION(60, AvailabilityStatus.NONE_STATUS),
    SCHEDULED_MAINTENANCE(70, AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS),
    PLANNED_CORRECTIVE_ACTION(80, AvailabilityStatus.NONE_STATUS),
    FORCED_OUTAGE(90, AvailabilityStatus.FORCED_OUTAGE_STATUS),
    SUSPENDED(100, AvailabilityStatus.NONE_STATUS),
    FORCE_MAJEURE(110, AvailabilityStatus.NONE_STATUS),
    INFORMATION_UNAVAILABLE(120, AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS);

    public static final String NORMAL_STATUS = "normal status";
    public static final String REQUESTED_SHUTDOWN_STATUS = "requested shutdown";
    public static final String FORCED_OUTAGE_STATUS = "forced outage";
    public static final String SCHEDULED_MAINTENANCE_STATUS = "scheduled maintenance";
    public static final String INFORMATION_UNAVAILABLE_STATUS = "information unavailable";
    public static final String NONE_STATUS = "";

    @Getter
    private final int value;
    @Getter
    private final String groupName;

    AvailabilityStatus(int value) {
        this.value = value;
        this.groupName = null;
    }

    AvailabilityStatus(int value, String groupName) {
        this.value = value;
        this.groupName = groupName;
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

    public static List<Integer> getStatusCodes(String groupName){
        List<Integer> result = new ArrayList<>();

        for(AvailabilityStatus availabilityStatus : AvailabilityStatus.values())
        {
            if(availabilityStatus.groupName.compareTo(groupName) == 0){
                result.add(availabilityStatus.value);
            }
        }
        return result;
    }
}