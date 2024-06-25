package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.availability.AvailabilityService;
import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.variable.ConstantVariable;
import com.unison.scada.availability.api.availability.variable.Variable;
import com.unison.scada.availability.api.availability.variable.VariableRepository;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.reports.daily.DailyReportDTO;
import com.unison.scada.availability.api.reports.daily.DailyReportGenerateService;
import com.unison.scada.availability.api.reports.daily.DailyReportService;
import com.unison.scada.availability.api.reports.memo.MemoReportDTO;
import com.unison.scada.availability.api.reports.memo.MemoReportGenerateService;
import com.unison.scada.availability.api.reports.memo.MemoReportService;
import com.unison.scada.availability.api.reports.statics.StaticReportDTO;
import com.unison.scada.availability.api.reports.statics.StaticReportGenerateService;
import com.unison.scada.availability.api.reports.statics.StaticReportService;
import com.unison.scada.availability.api.windfarm.WindFarmOverview;
import com.unison.scada.availability.api.windfarm.WindFarmOverviewRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.api.windfarm.WindFarmService;
import com.unison.scada.availability.global.General;
import com.unison.scada.availability.global.ReportExcelGenerator;
import com.unison.scada.availability.global.NullUtil;
import com.unison.scada.availability.global.filter.GeneralRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService implements StaticReportService, StaticReportGenerateService, MemoReportService, MemoReportGenerateService, DailyReportService, DailyReportGenerateService {
    private final AvailabilityDataRepository availabilityDataRepository;
    private final WindFarmOverviewRepository windFarmOverviewRepository;
    private final GeneralRepository generalRepository;
    private final MemoRepository memoRepository;
    private final ReportDataInMemory reportDataInMemory;
    private final WindFarmProperties windFarmProperties;
    private final AvailabilityService availabilityService;
    private final VariableRepository variableRepository;
    private final ReportsMapper reportsMapper;

    @Override
    public StaticReportDTO.Response getStaticReportData(Principal principal, ReportsDTO.Request request) throws Exception {
        StaticReportDTO.Response response;

        LocalDateTime startTime = createLocalDateTime(request.getStartDate());
        LocalDateTime endTime = createLocalDateTime(request.getEndDate()).plusDays(1);

        checkStartTimeAndEndTime(startTime, endTime);

        /*
         * Turbine Information Inquiry
         * */
        String windFarmName = request.getWindFarmName();
        WindFarmOverview windFarmOverview = windFarmOverviewRepository.findFirstByName(windFarmName.toLowerCase());

        Integer windFarmId = windFarmOverview.getWindFarmId();
        Integer turbineId = request.getTurbineId();
        List<AvailabilityData> availabilityDataList;

        // 특정 turbine 데이터만 조회
        if(request.getDeviceType().equalsIgnoreCase("wind farm")){
            availabilityDataList = availabilityDataRepository.findByWindFarmIdAndTimeBetween(windFarmId, startTime, endTime);
        }
        //wind farm 데이터 조회
        else if(request.getDeviceType().equalsIgnoreCase("wind turbine")){
            availabilityDataList = availabilityDataRepository.findByWindFarmIdAndTurbineIdAndTimeBetween(windFarmId, turbineId, startTime, endTime);
        }
        else {
            availabilityDataList = new ArrayList<>();
        }

        response = reportsMapper.toStaticReportDTOResponse(availabilityDataList, request.getReportType());

        /*
         * Save Static data to create excel
         * */
        reportDataInMemory.getReportDataMap().put(principal.getName(), response);

        return response;
    }

    @Override
    public MemoReportDTO.Response getMemoReportData(Principal principal, ReportsDTO.Request request) throws Exception {
        MemoReportDTO.Response response;

        LocalDateTime startTime = createLocalDateTime(request.getStartDate());
        LocalDateTime endTime = createLocalDateTime(request.getEndDate()).plusDays(1);

        checkStartTimeAndEndTime(startTime, endTime);

        /*
        * Turbine Information Inquiry
        * */
        String windFarmName = request.getWindFarmName();
        WindFarmOverview windFarmOverview = windFarmOverviewRepository.findFirstByName(windFarmName.toLowerCase());

        Integer windFarmId = windFarmOverview.getWindFarmId();
        Integer turbineId = request.getTurbineId();
        List<Memo> memoList;

        // 특정 turbine 데이터만 조회
        if(request.getDeviceType().equalsIgnoreCase("wind farm"))
        {
            memoList = memoRepository.findByMemoIdWindFarmIdAndMemoIdTimestampBetweenOrderByMemoIdTimestampAsc(windFarmId, startTime, endTime);
        }
        //wind farm 데이터 조회
        else if(request.getDeviceType().equalsIgnoreCase("wind turbine"))
        {
            memoList = memoRepository.findByIdBetween(windFarmId, turbineId, startTime, endTime);
        }
        else
        {
            memoList = new ArrayList<>();
        }

        response = reportsMapper.memoToReportsDTOResponse(memoList);

        /*
         * Save memo data to create excel
         * */
        reportDataInMemory.getReportDataMap().put(principal.getName(), response);

        return response;
    }


    @Override
    public DailyReportDTO.Response getDailyReportData(Principal principal, DailyReportDTO.Request request) throws Exception {
        DailyReportDTO.Response response = new DailyReportDTO.Response();

        String[] dailyDateSplits = request.getDailyDate().split("_");

        LocalDateTime dailyStartTime = LocalDateTime.of(
                Integer.parseInt(dailyDateSplits[0]),
                Integer.parseInt(dailyDateSplits[1]),
                Integer.parseInt(dailyDateSplits[2]),
                0,
                0);

        LocalDateTime monthlyStartTime = LocalDateTime.of(
                Integer.parseInt(dailyDateSplits[0]),
                Integer.parseInt(dailyDateSplits[1]),
                1,
                0,
                0);

        LocalDateTime endTime = LocalDateTime.of(
                Integer.parseInt(dailyDateSplits[0]),
                Integer.parseInt(dailyDateSplits[1]),
                Integer.parseInt(dailyDateSplits[2]),
                0,
                0).plusDays(1);

        endTime = endTime.isAfter(LocalDateTime.now()) ? LocalDateTime.now() : endTime;

        Optional<Variable> optionalVariable = variableRepository.findById(ConstantVariable.TOTAL_PRODUCTION_POWER.getUuid());
        Variable variable = optionalVariable.orElseThrow(() ->  new Exception("Not matched total exported power name"));

        List<General> generalList = generalRepository.findAll();


        List<Double> dailyActualActivePowerList = new ArrayList<>();
        List<Double> monthlyActualActivePowerList = new ArrayList<>();

        for(General general : generalList)
        {
            double dailyActualActivePower;
            double monthlyActualActivePower;

            Optional<Double> dailyTotalPowerAtStartTime = availabilityDataRepository.getTimeAfterCertainTimestamp(general.getGeneralId().getTurbineId(), variable.getUuid(), dailyStartTime, endTime);
            Optional<Double> monthlyTotalPowerAtStartTime = availabilityDataRepository.getTimeAfterCertainTimestamp(general.getGeneralId().getTurbineId(), variable.getUuid(), monthlyStartTime, endTime);
            Optional<Double> totalPowerAtEndTime = availabilityDataRepository.getTimeBeforeCertainTimestamp(general.getGeneralId().getTurbineId(), variable.getUuid(), endTime, dailyStartTime);

            if(totalPowerAtEndTime.isEmpty() || dailyTotalPowerAtStartTime.isEmpty())
                dailyActualActivePower = 0d;
            else
                dailyActualActivePower = totalPowerAtEndTime.get() - dailyTotalPowerAtStartTime.get();

            if(totalPowerAtEndTime.isEmpty() || monthlyTotalPowerAtStartTime.isEmpty())
                monthlyActualActivePower = 0d;
            else
                monthlyActualActivePower = totalPowerAtEndTime.get() - monthlyTotalPowerAtStartTime.get();

            dailyActualActivePowerList.add(dailyActualActivePower);
            monthlyActualActivePowerList.add(monthlyActualActivePower);
        }

        /*
         * calc each turbine's daily availability
         * */
        List<WindFarmService.Avail> dailyAvailabilityList = availabilityService.getAvailabilityOfAllTurbine(dailyStartTime, endTime);

        /*
         * calc each turbine's monthly availability
         * */
        List<WindFarmService.Avail> monthlyAvailabilityList = availabilityService.getAvailabilityOfAllTurbine(monthlyStartTime, endTime);


        /*
         * Set DTO Response
         * */

        List<String> headerList = new ArrayList<>();

        headerList.add("호기");
        headerList.add("발전량 [kWh]");
        headerList.add("가동률 [%]");
        headerList.add("이용률 [%]");
        headerList.add("월 누적 발전량 [kWh]");
        headerList.add("월 누적 이용률 [%]");
        headerList.add("월 누적 가동률 [%]");


        /*
         * Set total turbine data of table
         * */
        List<DailyReportDTO.Response.DailyData> dailyDataList = new ArrayList<>();

        dailyDataList.add(DailyReportDTO.Response.DailyData.builder()
                .deviceName("TOTAL")
                .dailyProduction(String.format("%.02f",dailyActualActivePowerList.stream().mapToDouble(Double::doubleValue).sum() * 1000d))
                .dailyAvailability(String.format("%.02f", WindFarmService.Avail.average(dailyAvailabilityList)))
                .dailyCapacityFactor(String.format("%.02f", getTotalCapacityFactor(dailyStartTime, endTime, dailyActualActivePowerList)))
                .monthlyProduction(String.format("%.02f",monthlyActualActivePowerList.stream().mapToDouble(Double::doubleValue).sum() * 1000d))
                .monthlyAvailability(String.format("%.02f", WindFarmService.Avail.average(monthlyAvailabilityList)))
                .monthlyCapacityFactor(String.format("%.02f", getTotalCapacityFactor(monthlyStartTime, endTime, monthlyActualActivePowerList)))
                .build());



        List<Double> dailyCapacityFactorList = getCapacityFactor(dailyStartTime, endTime, dailyActualActivePowerList);
        List<Double> monthlyCapacityFactorList =  getCapacityFactor(monthlyStartTime, endTime, monthlyActualActivePowerList);

        /*
         * Set each turbine data of table
         * */
        for(int turbineId = 0; turbineId < windFarmProperties.getTurbinesNumber(); turbineId++)
        {
            DailyReportDTO.Response.DailyData turbineDailyData = DailyReportDTO.Response.DailyData.builder()
                    .deviceName(String.format("WTG%02d", turbineId + 1))
                    .dailyProduction(String.format("%.02f", dailyActualActivePowerList.get(turbineId) * 1000))
                    .dailyAvailability(String.format("%.02f", dailyAvailabilityList.get(turbineId).getAvail()))
                    .dailyCapacityFactor(String.format("%.02f", dailyCapacityFactorList.get(turbineId)))
                    .monthlyProduction(String.format("%.02f", monthlyActualActivePowerList.get(turbineId) * 1000))
                    .monthlyAvailability(String.format("%.02f", monthlyAvailabilityList.get(turbineId).getAvail()))
                    .monthlyCapacityFactor(String.format("%.02f",monthlyCapacityFactorList.get(turbineId)))
                    .build();

            dailyDataList.add(turbineDailyData);
        }

        response.setHeaderList(headerList);
        response.setTableData(dailyDataList);
        response.setDate(dailyStartTime);

        /*
         * Save daily report data to create excel
         * */
        reportDataInMemory.getReportDataMap().put(principal.getName(), response);

        return response;
    }

    @Override
    public void generateStaticReportExcel(HttpServletResponse response, Principal principal) throws IOException {
        StaticReportDTO.Response staticReportDto = (StaticReportDTO.Response) reportDataInMemory.getReportDataMap().get(principal.getName());

        ReportExcelGenerator reportExcelGenerator = new ReportExcelGenerator();
        reportExcelGenerator.setHeaderCellBackgroundColor(new XSSFColor(new byte[]{(byte) 66, (byte) 125, (byte) 158}));

        /*
         * make header of table data
         * */
        List<String> headerNames = staticReportDto.getTableHeader().stream()
                                                                    .map(data ->
                                                                         data.getUnit() == null ?
                                                                                data.getName() :
                                                                                String.format("%s\n[%s]", data.getName(), data.getUnit())
                                                                    )
                                                                    .collect(Collectors.toList());

        reportExcelGenerator.setHeaderNames(headerNames);

        /*
        * make body of table data
        * */
        List<List<String>> bodyDatass = staticReportDto.getTableData().getRow()
                .stream()
                .map(
                        StaticReportDTO.Response.TableDataRow.TableDataItem::getValue
                )
                .toList();

        reportExcelGenerator.setBodyDatas(bodyDatass);

        /*
         * Download excel of static report
         */
        Workbook workbook = reportExcelGenerator.getWorkbook();

        String fileName = "Static Report";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();

    }
    @Override
    public void generateDailyReportExcel(HttpServletResponse response, Principal principal) throws IOException {

        DailyReportDTO.Response dailyReportDto = (DailyReportDTO.Response) reportDataInMemory.getReportDataMap().get(principal.getName());

        /*
        * Set sheet name
        * */
        LocalDateTime dailyReportDate = dailyReportDto.getDate();
        String sheetName = String.format("%d.%d.%d", dailyReportDate.getYear(), dailyReportDate.getMonthValue(), dailyReportDate.getDayOfMonth());

        ReportExcelGenerator reportExcelGenerator = new ReportExcelGenerator(sheetName);

        reportExcelGenerator.setHeaderCellBackgroundColor(new XSSFColor(new byte[]{(byte) 66, (byte) 125, (byte) 158}));

        /*
         * make header and body datas
         * */

        reportExcelGenerator.setHeaderNames(dailyReportDto.getHeaderList());

        List<List<String>> bodyDatass = new ArrayList<>();
        List<DailyReportDTO.Response.DailyData> dailyDataList = dailyReportDto.getTableData();

        for(DailyReportDTO.Response.DailyData dailyData : dailyDataList){
            bodyDatass.add(new ArrayList<>(Arrays.asList(
                    NullUtil.ifNullEmptyString(dailyData.getDeviceName()),
                    NullUtil.ifNullEmptyString(dailyData.getDailyProduction()),
                    NullUtil.ifNullEmptyString(dailyData.getDailyAvailability()),
                    NullUtil.ifNullEmptyString(dailyData.getDailyCapacityFactor()),
                    NullUtil.ifNullEmptyString(dailyData.getMonthlyProduction()),
                    NullUtil.ifNullEmptyString(dailyData.getMonthlyCapacityFactor()),
                    NullUtil.ifNullEmptyString(dailyData.getMonthlyAvailability())
            )));
        }
        reportExcelGenerator.setBodyDatas(bodyDatass);

        /**
         * download
         */
        Workbook workbook = reportExcelGenerator.getWorkbook();

        String fileName = "Daily Report";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    @Override
    public void generateMemoReportExcel(HttpServletResponse response, Principal principal) throws IOException {
        ReportExcelGenerator reportExcelGenerator = new ReportExcelGenerator();

        reportExcelGenerator.setHeaderCellBackgroundColor(new XSSFColor(new byte[]{(byte) 66, (byte) 125, (byte) 158}));

        /*
         * make header and body datas
         * */

        MemoReportDTO.Response memoDto = (MemoReportDTO.Response) reportDataInMemory.getReportDataMap().get(principal.getName());
        reportExcelGenerator.setHeaderNames(memoDto.getHeaderList());

        List<List<String>> bodyDatass = new ArrayList<>();
        List<MemoReportDTO.Response.MemoData> memoDataList = memoDto.getTableData();

        for(MemoReportDTO.Response.MemoData memoData : memoDataList){
            bodyDatass.add(new ArrayList<>(Arrays.asList(
                    NullUtil.ifNullEmptyString(memoData.getTimeStamp()),
                    NullUtil.ifNullEmptyString(memoData.getDeviceName()),
                    NullUtil.ifNullEmptyString(memoData.getEngineerName()),
                    NullUtil.ifNullEmptyString(memoData.getWorkTime()),
                    NullUtil.ifNullEmptyString(memoData.getMaterial()),
                    NullUtil.ifNullEmptyString(memoData.getQuantity()),
                    NullUtil.ifNullEmptyString(memoData.getWorkType()),
                    NullUtil.ifNullEmptyString(memoData.getInspection()),
                    NullUtil.ifNullEmptyString(memoData.getEtc())
            )));
        }
        reportExcelGenerator.setBodyDatas(bodyDatass);

        /**
         * download
         */
        Workbook workbook = reportExcelGenerator.getWorkbook();

        String fileName = "Memo Report";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
    private List<Double> getCapacityFactor(LocalDateTime startTime, LocalDateTime endTime, List<Double> actualActivePowerList) throws Exception {
        List<Double> results = new ArrayList<>();
        List<General> generalList = generalRepository.findAll();

        long durationSeconds = Duration.between(startTime, endTime).toSeconds();

        for(int turbineId = 0; turbineId < generalList.size(); turbineId++)
        {
            double potentialActivePower = (generalList.get(turbineId).getRatedPower() * ((double) durationSeconds / 3600));

            results.add(actualActivePowerList.get(turbineId) / potentialActivePower * 100);
        }
        return results;
    }

    private double getTotalCapacityFactor(LocalDateTime startTime, LocalDateTime endTime, List<Double> actualActivePowerList) throws Exception {
        List<General> generalList = generalRepository.findAll();
        long durationSeconds = Duration.between(startTime, endTime).toSeconds();

        double potentialActivePower = generalList.stream()
                .mapToDouble(value -> value.getRatedPower() * ((double) durationSeconds / 3600))
                .sum();

        double actualActivePower = actualActivePowerList.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        return (actualActivePower / potentialActivePower) * 100;
    }

    private LocalDateTime createLocalDateTime(String time){
        String[] splitTime = time.split("_");

        return LocalDateTime.of(
                Integer.parseInt(splitTime[0]),
                Integer.parseInt(splitTime[1]),
                Integer.parseInt(splitTime[2]),
                0,
                0);
    }

    private void checkStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        if(endTime.isBefore(startTime)){
            throw new Exception("종료 시간이 시작 시간보다 이전입니다. 올바른 시간을 입력하세요.");
        }
    }


}
