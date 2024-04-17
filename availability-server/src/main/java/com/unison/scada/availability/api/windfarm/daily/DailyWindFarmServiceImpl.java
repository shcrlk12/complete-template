package com.unison.scada.availability.api.windfarm.daily;

import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyWindFarmServiceImpl implements DailyWindFarmService {

    private final WindFarmProperties windFarmProperties;
    @Override
    public DailyWindFarmDTO.Response getWindFarmGeneralInfo() {

        return DailyWindFarmDTO.Response.builder()
                .turbineNumber(windFarmProperties.getTurbinesNumber())
                .build();
    }
}
