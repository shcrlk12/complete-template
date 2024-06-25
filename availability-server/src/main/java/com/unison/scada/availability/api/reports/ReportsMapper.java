package com.unison.scada.availability.api.reports;


import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.availability.variable.ConstantVariable;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.reports.memo.MemoReportDTO;
import com.unison.scada.availability.api.reports.statics.StaticReportDTO;
import com.unison.scada.availability.api.windfarm.WindFarmService;
import com.unison.scada.availability.global.DateTimeUtil;
import com.unison.scada.availability.global.NullUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReportsMapper {

    private final AvailabilityTypeRepository availabilityTypeRepository;

    public MemoReportDTO.Response.MemoData memoToReportsDTOResponse(Memo memo){

        return MemoReportDTO.Response.MemoData.builder()
                .timeStamp(DateTimeUtil.formatToYearMonthDayHourMinute(memo.getMemoId().getTimestamp()))
                .deviceName(String.format("WTG%02d", memo.getMemoId().getTurbineId() + 1))
                .engineerName(memo.getEngineerName())
                .workTime(DateTimeUtil.formatToYearMonthDayHourMinute(memo.getWorkTime()))
                .material(memo.getMaterial())
                .quantity(memo.getQuantity())
                .workType(memo.getWorkType())
                .inspection(memo.getInspection())
                .etc(memo.getEtc())
                .build();
    }

    public MemoReportDTO.Response memoToReportsDTOResponse(List<Memo> memo){

        List<MemoReportDTO.Response.MemoData> memoDataList = memo.stream()
                .map(this::memoToReportsDTOResponse)
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


    public StaticReportDTO.Response toStaticReportDTOResponse(List<AvailabilityData> availabilityDataList, String reportType){

        StaticReportDTO.Response response = new StaticReportDTO.Response();

        /*
        * Set Header Data
        * */
        List<StaticReportDTO.Response.TableHeader> headerList = new ArrayList<>();

        /*
        * Fixed header
        * */
        headerList.add(new StaticReportDTO.Response.TableHeader("Time", null));
        headerList.add(new StaticReportDTO.Response.TableHeader("Device Name", null));
        headerList.add(new StaticReportDTO.Response.TableHeader("Wind Speed", "m/s"));
        headerList.add(new StaticReportDTO.Response.TableHeader("Energy Production", "MWh"));
        headerList.add(new StaticReportDTO.Response.TableHeader("Availability", "%"));
        headerList.add(new StaticReportDTO.Response.TableHeader("Capacity Factor", "%"));

        List<AvailabilityType> availabilityTypeList = availabilityTypeRepository.findByIsActiveTrue();

        for(AvailabilityType availabilityType : availabilityTypeList ){
            headerList.add(new StaticReportDTO.Response.TableHeader(availabilityType.getName(), "min"));
        }

        /*
         * Set Body Data
         * */
        Map<LocalDateTime, Map<Integer, Map<String, List<AvailabilityData>>>> groupedData =
                availabilityDataList.stream()
                        .filter(data -> data.getAvailabilityType() != null)
                        .collect(Collectors.groupingBy(
                                data -> data.getAvailabilityDataId().getTimestamp(),
                                LinkedHashMap::new, // 외부 맵에 LinkedHashMap을 사용하여 순서 유지
                                Collectors.groupingBy(
                                        data -> data.getAvailabilityDataId().getTurbineId(),
                                        LinkedHashMap::new,
                                        Collectors.groupingBy(data -> data.getAvailabilityType().getName())
                                )
                        ));

        List<StaticReportDTO.Response.TableDataRow.TableDataItem> tableDataItemList = new ArrayList<>();

        availabilityDataList.forEach(data -> System.out.println(data.getVariable()));
        /*
        * Get turbines availability data
        * */

        RowData test = new RowData();
        test.setData(availabilityDataList);
        test.clacValue(reportType);

        Map<String, Map<Integer, Map<String, RowData.DataSet>>> test00 = test.getModifiedDataMap();

        for(String localDateTime : test00.keySet()){
            Map<Integer, Map<String, RowData.DataSet>> availabilityDataList2 = test00.get(localDateTime);

            for(Integer turbineId : availabilityDataList2.keySet()) {
                Map<String, RowData.DataSet> aaa2 = availabilityDataList2.get(turbineId);
                List<String> tableDataList = new ArrayList<>();

                for (AvailabilityType availabilityType : availabilityTypeList) {
                    /*
                     * First set time of table
                     * */
                    if (tableDataList.isEmpty()) {
                        tableDataList.add(localDateTime);
                        tableDataList.add(String.format("WTG%02d", turbineId + 1));
                        tableDataList.add(NullUtil.formatIfNullZero(test.getRowData(ConstantVariable.WIND_SPEED.getStringUuid(), localDateTime, turbineId), "%.2f")); // Wind Speed
                        tableDataList.add(NullUtil.formatIfNullZero(test.getEnergyProduction(localDateTime, turbineId), "%.2f")); // Energy Production
                        tableDataList.add(NullUtil.formatIfNullZero(test.getAvailability(localDateTime, turbineId), "%.2f")); // Availability
                        tableDataList.add(NullUtil.formatIfNullZero(test.getCapacityFactorMap(localDateTime, turbineId), "%.2f")); // Capacity Factor
                    }
                    if (aaa2.containsKey(availabilityType.getUuid().toString())) {
                        tableDataList.add(String.valueOf(aaa2.get(availabilityType.getUuid().toString()).convertValue().intValue()));
                    } else {
                        tableDataList.add("0");
                    }
                }
                tableDataItemList.add(StaticReportDTO.Response.TableDataRow.TableDataItem.builder()
                        .value(tableDataList)
                        .build());
            }
        }

        StaticReportDTO.Response.TableDataRow tableDataRow = StaticReportDTO.Response.TableDataRow.builder()
                .row(tableDataItemList)
                .build();

        response.setTableHeader(headerList);
        response.setTableData(tableDataRow);

        return response;
    }
}
