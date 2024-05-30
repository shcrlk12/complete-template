package com.unison.scada.availability.scheduler.availability.save;

import com.unison.scada.availability.scheduler.availability.model.Turbine;

import java.util.List;

public interface AvailabilitySaveService {
    void save1HourAvailabilityTotalTime(int turbinesNumber);

    void saveTurbineData(List<Turbine> turbineList);
}
