package com.unison.scada.availability.api.availability;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public interface AnnuallyWindFarmService {
    AnnuallyWindFarmDTO.Response getWindFarmGeneralInfo(LocalDateTime searchTime);
}
