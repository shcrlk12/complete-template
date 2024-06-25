package com.unison.scada.availability.global;

public class TurbineCalcUtil {

    public static Double getCapacityFactor(Double actualEnergyProduction, Double ratedPower, Double period){
        double result = (actualEnergyProduction / (ratedPower * (period / 3600)))  * 100;

        return result > 100 ? 100 : result;
    }
}
