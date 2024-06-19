package com.unison.scada.availability.api.availability;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ConstantVariable {

    TURBINE_MAIN_STATUS("307f3d53-bf04-410d-9333-5d9aed5e3133"),
    WIND_SPEED("9d35232c-1971-47c2-ad99-2d511053994f"),
    TOTAL_PRODUCTION_POWER("1c6ab584-ad0c-46a0-acaf-02a10abbe183");

    private final String uuid;

    @Setter
    private String opcVariableName;
    ConstantVariable(String uuid){
        this.uuid = uuid;
    }


}
