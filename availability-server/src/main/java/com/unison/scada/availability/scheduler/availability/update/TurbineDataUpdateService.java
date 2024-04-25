package com.unison.scada.availability.scheduler.availability.update;

import com.unison.scada.availability.comm.opcda.OPCException;
import com.unison.scada.availability.comm.opcda.OPCNotFoundException;
import com.unison.scada.availability.scheduler.availability.model.Turbine;

import java.util.List;

public interface TurbineDataUpdateService {

    Turbine getUpdatedTurbineData(int turbineId) throws OPCException, OPCNotFoundException;



    void updateTotalAvailability(List<Turbine> turbines) throws Exception;
}
