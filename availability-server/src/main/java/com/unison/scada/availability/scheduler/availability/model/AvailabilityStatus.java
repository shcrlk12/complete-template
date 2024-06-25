package com.unison.scada.availability.scheduler.availability.model;

import lombok.Getter;

import java.util.*;


public enum AvailabilityStatus{
    FULL_PERFORMANCE(10, AvailabilityStatus.NORMAL_TYPE, "6c5032bf-ce14-45a2-a14f-407ece68bfff"),
    PARTIAL_PERFORMANCE(20, AvailabilityStatus.NORMAL_TYPE, "6c5032bf-ce14-45a2-a14f-407ece68bfff"),
    TECHNICAL_STANDBY(30, AvailabilityStatus.NORMAL_TYPE, "6c5032bf-ce14-45a2-a14f-407ece68bfff"),
    OUT_OF_ENVIRONMENTAL(40, AvailabilityStatus.NORMAL_TYPE, "6c5032bf-ce14-45a2-a14f-407ece68bfff"),
    REQUESTED_SHUTDOWN(50, AvailabilityStatus.REQUESTED_SHUTDOWN_TYPE, "176f3039-eafb-4e61-9c73-d692beae94a7"),
    OUT_OF_ELECTRICAL_SPECIFICATION(60, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE, "195f866d-89e8-49f6-b111-bf50b1b67ff6"),
    SCHEDULED_MAINTENANCE(70, AvailabilityStatus.SCHEDULED_MAINTENANCE_TYPE, "32b8d402-1a55-4cea-be8d-65e7d0157f54"),
    PLANNED_CORRECTIVE_ACTION(80, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE, "195f866d-89e8-49f6-b111-bf50b1b67ff6"),
    FORCED_OUTAGE(90, AvailabilityStatus.FORCED_OUTAGE_TYPE, "3c0649a9-cc71-426a-849b-d60308ab98a9"),
    SUSPENDED(100, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE, "195f866d-89e8-49f6-b111-bf50b1b67ff6"),
    FORCE_MAJEURE(110, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE, "195f866d-89e8-49f6-b111-bf50b1b67ff6"),
    INFORMATION_UNAVAILABLE(120, AvailabilityStatus.INFORMATION_UNAVAILABLE_TYPE, "195f866d-89e8-49f6-b111-bf50b1b67ff6");

    public static final String NORMAL_TYPE = "6c5032bf-ce14-45a2-a14f-407ece68bfff";
    public static final String REQUESTED_SHUTDOWN_TYPE = "176f3039-eafb-4e61-9c73-d692beae94a7";
    public static final String FORCED_OUTAGE_TYPE = "3c0649a9-cc71-426a-849b-d60308ab98a9";
    public static final String SCHEDULED_MAINTENANCE_TYPE = "32b8d402-1a55-4cea-be8d-65e7d0157f54";
    public static final String INFORMATION_UNAVAILABLE_TYPE = "195f866d-89e8-49f6-b111-bf50b1b67ff6";

    @Getter
    private final int value;
    @Getter
    private final String type;
    @Getter
    private final String uuid;


    AvailabilityStatus(int value, String type, String uuid) {
        this.value = value;
        this.type = type;
        this.uuid = uuid;
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