package com.unison.scada.availability.global.scheduler.availability;


public interface TurbineData {

    int getTurbineId();

    AvailabilityStatus getCurrentAvailabilityStatus();

    double getCurrentActivePower();

    double getCurrentWindSpeed();

}
