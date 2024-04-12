package com.unison.scada.availability.global.scheduler.availability;

public class TurbineDataImpl implements TurbineData{

    int turbineId;

    AvailabilityStatus availabilityStatus;
    double activePower;
    double windSpeed;

    @Override
    public int getTurbineId() {
        return turbineId;
    }
    @Override
    public AvailabilityStatus getCurrentAvailabilityStatus() {
        return availabilityStatus;
    }

    @Override
    public double getCurrentActivePower() {
        return activePower;
    }

    @Override
    public double getCurrentWindSpeed() {
        return windSpeed;
    }


}
