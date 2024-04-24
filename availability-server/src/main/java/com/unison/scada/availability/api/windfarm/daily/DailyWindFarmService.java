package com.unison.scada.availability.api.windfarm.daily;

import java.time.LocalDateTime;

public interface DailyWindFarmService {

    DailyWindFarmDTO.Response getDailyWindFarmGeneralInfo(LocalDateTime searchTime);
}
