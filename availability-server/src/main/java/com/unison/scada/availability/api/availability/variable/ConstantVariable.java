package com.unison.scada.availability.api.availability.variable;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public enum ConstantVariable {

    TURBINE_MAIN_STATUS("307f3d53-bf04-410d-9333-5d9aed5e3133"),
    WIND_SPEED("9d35232c-1971-47c2-ad99-2d511053994f"),
    TOTAL_PRODUCTION_POWER("1c6ab584-ad0c-46a0-acaf-02a10abbe183");

    private final String uuid;

    @Getter
    private String opcVariableName;

    @Getter
    @Setter
    private boolean isSave;

    ConstantVariable(String uuid){
        this.uuid = uuid;
    }

    public static void initializeConstant(List<Variable> variableList){

        Map<String, Variable> variableMap = variableList.stream().collect(Collectors.toMap(value -> value.getUuid().toString(), value -> value));

        for(ConstantVariable constantVariable : ConstantVariable.values()){
            constantVariable.opcVariableName = variableMap.get(constantVariable.getUuid().toString()).getName();
            constantVariable.isSave = variableMap.get(constantVariable.getUuid().toString()).isSave();
        }
    }

    public UUID getUuid(){
        return UUID.fromString(uuid);
    }

    public String getStringUuid(){
        return uuid;
    }
}
