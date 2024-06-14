package com.unison.scada.availability.api.reports;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class MemoReportDataInMemory {

    private final Map<String, MemoReportDTO.Response> memoReportDataMap = new HashMap<>();
}
