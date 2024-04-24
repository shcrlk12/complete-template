package com.unison.scada.availability.api.windfarm.annually;

import java.time.LocalDateTime;


public interface AnnuallyWindFarmService {
    AnnuallyWindFarmDTO.Response getAnnuallyWindFarmGeneralInfo(LocalDateTime searchTime);
}
