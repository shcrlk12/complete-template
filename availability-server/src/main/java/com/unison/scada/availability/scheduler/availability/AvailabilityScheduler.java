package com.unison.scada.availability.scheduler.availability;

import com.unison.scada.availability.comm.opcda.OPCException;
import com.unison.scada.availability.comm.opcda.OPCNotFoundException;
import com.unison.scada.availability.comm.opcda.OPCServer;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
import com.unison.scada.availability.scheduler.availability.model.WindFarm;
import com.unison.scada.availability.scheduler.availability.save.AvailabilitySaveService;
import com.unison.scada.availability.scheduler.availability.update.TurbineDataUpdateByOPCService;
import com.unison.scada.availability.scheduler.availability.update.TurbineDataUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.System.exit;

@Component
@RequiredArgsConstructor
public class AvailabilityScheduler {
    Logger logger = LoggerFactory.getLogger(AvailabilityScheduler.class);

    private final TurbineDataUpdateService turbineDataUpdateService;
    private final AvailabilitySaveService availabilitySaveService;
    private final WindFarm windFarm;
    private final SchedulerProperties schedulerProperties;

    // update wind farm data by every 2 seconds
    @Scheduled(fixedRate = 2000)
    public void run() throws OPCException {
        try{

            List<Turbine> turbines = windFarm.getTurbinesData();

            //위에서 가져온 데이터를 가지고 availability 계산 +2
            turbineDataUpdateService.updateTotalAvailability(windFarm.getTurbines());
        }catch (OPCNotFoundException | OPCException e){
            logger.error(e.getMessage(), e);
            System.exit(-1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //save availability data by every hour
    @Scheduled(cron = "0 0 * * * *")
    public void oneHourScheduler(){
        availabilitySaveService.save1HourAvailabilityTotalTime(windFarm.getTurbines().size());

    }
}
