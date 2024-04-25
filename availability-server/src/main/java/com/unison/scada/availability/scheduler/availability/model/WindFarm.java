package com.unison.scada.availability.scheduler.availability.model;

import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.comm.opcda.OPCException;
import com.unison.scada.availability.comm.opcda.OPCNotFoundException;
import com.unison.scada.availability.scheduler.availability.update.TurbineDataUpdateService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class WindFarm {

    private final WindFarmProperties windFarmProperties;
    private final TurbineDataUpdateService turbineDataUpdateService;

    @Getter
    private List<Turbine> turbines;

    @PostConstruct
    private void initialize(){
        int turbinesNumber = windFarmProperties.getTurbinesNumber();

        turbines = IntStream.range(0, turbinesNumber)
                .mapToObj(Turbine::new)
                .collect(Collectors.toList());
    }

    public List<Turbine> getTurbinesData() throws OPCException, OPCNotFoundException {

        List<Turbine> turbineList = new ArrayList<>();

        for (Turbine turbine : turbines){
            turbineList.add(turbineDataUpdateService.getUpdatedTurbineData(turbine.getTurbineId()));
        }

        this.turbines = turbineList;

        return this.turbines;
    }

}
