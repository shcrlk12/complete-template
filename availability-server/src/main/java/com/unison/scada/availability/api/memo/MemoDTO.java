package com.unison.scada.availability.api.memo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemoDTO {
    private final int turbineId;
    private final LocalDateTime time;
    private final String engineerName;
    private final LocalDateTime workTime;
    private final String material;
    private final int quantity;
    private final String workType;
    private final String inspection;
    private final String etc;
}
