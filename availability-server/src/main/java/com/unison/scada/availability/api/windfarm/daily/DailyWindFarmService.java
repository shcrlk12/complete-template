package com.unison.scada.availability.api.windfarm.daily;

import java.security.Principal;
import java.time.LocalDateTime;

public interface DailyWindFarmService {

    DailyWindFarmDTO.Response getDailyWindFarmGeneralInfo(LocalDateTime searchTime);

    void registerDailyInfo(Principal principal, DailyWindFarmDTO.Request request) throws Exception;
}
