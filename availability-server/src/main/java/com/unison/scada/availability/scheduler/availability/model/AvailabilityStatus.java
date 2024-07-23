package com.unison.scada.availability.scheduler.availability.model;

import lombok.Getter;

import java.util.*;


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

    public static final String NORMAL_TYPE = "0A5DC96F-70A3-49CD-87F5-1BEAC1119B8D";
    public static final String REQUESTED_SHUTDOWN_TYPE = "22FBB3F3-0EF5-4B0A-A300-4EE3A9A8DA9E";
    public static final String FORCED_OUTAGE_TYPE = "770365D4-0157-4D12-9F14-F6069A1D6AD5";
    public static final String SCHEDULED_MAINTENANCE_TYPE = "83FF2124-E5F0-4761-9397-3AFA040AE2F3";
    public static final String INFORMATION_UNAVAILABLE_TYPE = "1FF874A5-E5C5-4DEB-A30D-C73D0A47C419";
    public static final String ETC_TYPE = "15CFC683-5FCB-42FA-B93D-E46C759B8103";
    public static final String LOW_TEMPERATURE_TYPE = "E742E4C5-4416-42F7-B5E5-FCB378E61F08";

    @Getter
    private final int value;
    @Getter
    private final String type;


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