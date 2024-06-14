package com.unison.scada.availability.api.reports;


import com.unison.scada.availability.api.memo.Memo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsMapper {

    public static MemoReportDTO.Response.MemoData memoToReportsDTOResponse(Memo memo){

        MemoReportDTO.Response.MemoData response;

        return MemoReportDTO.Response.MemoData.builder()
                .timeStamp(memo.getMemoId().getTimestamp())
                .deviceName(String.format("WTG%02d", memo.getMemoId().getTurbineId() + 1))
                .engineerName(memo.getEngineerName())
                .workTime(memo.getWorkTime())
                .material(memo.getMaterial())
                .quantity(memo.getQuantity())
                .workType(memo.getWorkType())
                .inspection(memo.getInspection())
                .etc(memo.getEtc())
                .build();
    }

    public static MemoReportDTO.Response memoToReportsDTOResponse(List<Memo> memo){

        List<MemoReportDTO.Response.MemoData> memoDataList = memo.stream()
                .map(ReportsMapper::memoToReportsDTOResponse)
                .collect(Collectors.toList());

        List<String> headerList = new ArrayList<>();

        headerList.add("Time");
        headerList.add("Device Name");
        headerList.add("작업자");
        headerList.add("작업 시간");
        headerList.add("자재");
        headerList.add("수량");
        headerList.add("작업 유형");
        headerList.add("점검 내역");
        headerList.add("기타(비고)");

        return MemoReportDTO.Response.builder()
                .headerList(headerList)
                .tableData(memoDataList)
                .build();
    }


}
