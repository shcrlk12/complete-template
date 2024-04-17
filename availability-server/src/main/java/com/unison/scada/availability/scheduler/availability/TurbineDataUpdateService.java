package com.unison.scada.availability.scheduler.availability;

import com.unison.scada.availability.comm.opcda.OPCException;
import com.unison.scada.availability.comm.opcda.OPCNotFoundException;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import com.unison.scada.availability.scheduler.availability.model.Turbine;

import java.util.List;
import java.util.Map;

public interface TurbineDataUpdateService {

    Turbine getUpdatedTurbineData(int turbineId) throws OPCException, OPCNotFoundException;

    void saveAHourAvailability(int turbinesNumber);

    void updateTotalAvailability(List<Turbine> turbines) throws Exception;
}
