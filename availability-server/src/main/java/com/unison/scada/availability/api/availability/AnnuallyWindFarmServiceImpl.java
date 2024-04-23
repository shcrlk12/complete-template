package com.unison.scada.availability.api.availability;

import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.scheduler.availability.model.AvailabilityStatus;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnuallyWindFarmServiceImpl implements AnnuallyWindFarmService{
    private final ParameterRepository parameterRepository;
    private final WindFarmProperties windFarmProperties;
    private final AvailabilityDataRepository availabilityDataRepository;

    @Override
    public AnnuallyWindFarmDTO.Response getWindFarmGeneralInfo(LocalDateTime searchTime) {
        List<Parameter> parameters = parameterRepository.findAll();

        Parameter parameter = parameters.get(0);

        Duration duration = Duration.between(parameter.getWarrantyDate(), searchTime);

        int howManyYearsOfWarranty = (int)(duration.toDays() / 365) + 1;

        LocalDateTime startTimeOfWarranty = parameter.getWarrantyDate().plusYears((duration.toDays() / 365));

        int turbinesNumber = windFarmProperties.getTurbinesNumber();
        List<Map<LocalDateTime, Avail>> turbineAvailTotal = getMap(startTimeOfWarranty);

        //set turbines
        List<AnnuallyWindFarmDTO.Response.Turbine> turbines = new ArrayList<>();
        double numerator = 0; //분자
        double denominator = 0; //분모
        double etcTime = 0;

        for(int i = 0; i < windFarmProperties.getTurbinesNumber(); i++) {
            Map<LocalDateTime, Avail> availMap = turbineAvailTotal.get(i);

            //set data
            List<AnnuallyWindFarmDTO.Response.Data> dataList = new ArrayList<>();
            LocalDateTime endTime = LocalDateTime.now().isBefore(startTimeOfWarranty.plusYears(1)) ? LocalDateTime.now() : startTimeOfWarranty.plusYears(1);

            for(LocalDateTime time = startTimeOfWarranty; time.isBefore(endTime); time = time.plusDays(1)){
                double turbineAvailability = 0;

                if(availMap.containsKey(time)){
                    Avail avail = availMap.get(time);

                    numerator += avail.getNumerator();
                    denominator += avail.getDenominator();
                    etcTime += avail.getEtcTime();

                    turbineAvailability = avail.getAvail();
                }

                dataList.add(new AnnuallyWindFarmDTO.Response.Data(time,  turbineAvailability));
            }

            double windFarmAvailability;

            try {
                windFarmAvailability = Avail.calcFormula(numerator, denominator, etcTime);
            }catch (Exception e){
                windFarmAvailability = 0;
            }

            turbines.add(new AnnuallyWindFarmDTO.Response.Turbine(i + 1, windFarmAvailability, dataList));
        }


        return AnnuallyWindFarmDTO.Response.builder()
                .turbinesNumber(turbinesNumber)
                .yearsOfWarranty(howManyYearsOfWarranty)
                .startTimeOfYears(startTimeOfWarranty)
                .turbines(turbines)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Avail{
        private double numerator; //분자
        private double denominator; //분모
        private double etcTime;

        public static double calcFormula(double numerator, double denominator, double etcTime){
            return (1 - (numerator / (etcTime - denominator))) * 100;
        }

        public double getAvail(){
            return Avail.calcFormula(numerator, denominator, etcTime);
        }

        public void calcAvailability(AvailabilityData availabilityData){

            if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.NORMAL_STATUS)){
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.FORCED_OUTAGE_STATUS))
            {
                numerator += availabilityData.getTime();
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.REQUESTED_SHUTDOWN_STATUS))
            {
                denominator += availabilityData.getTime();
            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.SCHEDULED_MAINTENANCE_STATUS))
            {

            }
            else if(availabilityData.getAvailabilityType().getName().equalsIgnoreCase(AvailabilityStatus.INFORMATION_UNAVAILABLE_STATUS))
            {
            }
            else
            {
            }
            etcTime += availabilityData.getTime();
        }

    }

    private List<Map<LocalDateTime, Avail>> getMap(LocalDateTime startTime){
        List<AvailabilityData> availabilityDataList = availabilityDataRepository.findAllDataByTimeRange(startTime, startTime.plusYears(1));
        Map<Integer, List<AvailabilityData>> listMap = availabilityDataList.stream()
                .collect(Collectors.groupingBy((data) -> data.getAvailabilityDataId().getTurbineId()));

        List<Map<LocalDateTime, Avail>> turbines = new ArrayList<>();

        for(int i = 1; i <= windFarmProperties.getTurbinesNumber(); i++)
        {
            Map<LocalDateTime, Avail> data = new HashMap<>();
            List<AvailabilityData> availabilityDataList1 = listMap.get(i);

            for(AvailabilityData availabilityData : availabilityDataList1)
            {
                LocalDateTime day = availabilityData.getAvailabilityDataId().getTimestamp().withHour(0).withMinute(0).withSecond(0);

                if(!data.containsKey(day)){
                    Avail a = new Avail();
                    a.calcAvailability(availabilityData);

                    data.put(day, a);
                }
                else {
                    Avail a = data.get(day);
                    a.calcAvailability(availabilityData);
                }
            }
            turbines.add(data);
        }

        return turbines;
    }

    private int getHowManyYearsOfWarranty(LocalDateTime searchTime){

        return 0;
    }
}
