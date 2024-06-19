package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.availability.AvailabilityService;
import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import com.unison.scada.availability.api.availability.repository.AvailabilityDataRepository;
import com.unison.scada.availability.api.availability.repository.AvailabilityTypeRepository;
import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.reports.daily.DailyReportDTO;
import com.unison.scada.availability.api.reports.daily.DailyReportGenerateService;
import com.unison.scada.availability.api.reports.daily.DailyReportService;
import com.unison.scada.availability.api.reports.memo.MemoReportDTO;
import com.unison.scada.availability.api.reports.memo.MemoReportGenerateService;
import com.unison.scada.availability.api.reports.memo.MemoReportService;
import com.unison.scada.availability.api.windfarm.WindFarmOverview;
import com.unison.scada.availability.api.windfarm.WindFarmOverviewRepository;
import com.unison.scada.availability.api.windfarm.WindFarmProperties;
import com.unison.scada.availability.api.windfarm.WindFarmService;
import com.unison.scada.availability.api.windfarm.daily.DailyWindFarmDTO;
import com.unison.scada.availability.global.General;
import com.unison.scada.availability.global.ReportExcelGenerator;
import com.unison.scada.availability.global.Util;
import com.unison.scada.availability.global.filter.GeneralRepository;
import com.unison.scada.availability.scheduler.availability.model.Turbine;
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

@Service
@RequiredArgsConstructor
public class ReportsService implements StaticReportService, MemoReportService, MemoReportGenerateService, DailyReportService, DailyReportGenerateService {
    private final AvailabilityDataRepository availabilityDataRepository;
    private final AvailabilityTypeRepository availabilityTypeRepository;
    private final WindFarmOverviewRepository windFarmOverviewRepository;
    private final GeneralRepository generalRepository;
    private final MemoRepository memoRepository;
    private final ReportDataInMemory reportDataInMemory;
    private final WindFarmProperties windFarmProperties;
    private final AvailabilityService availabilityService;

    @Override
    public ReportsDTO.Response getStaticReportData(Principal principal,ReportsDTO.Request request) {
        return null;
    }

    @Override
    public MemoReportDTO.Response getMemoReportData(Principal principal, ReportsDTO.Request request) throws Exception {
        MemoReportDTO.Response response;

        String[] startDateSplits = request.getStartDate().split("_");
        String[] endDateSplits = request.getEndDate().split("_");

        LocalDateTime startTime = LocalDateTime.of(
                Integer.parseInt(startDateSplits[0]),
                Integer.parseInt(startDateSplits[1]),
                Integer.parseInt(startDateSplits[2]),
                0,
                0);

        LocalDateTime endTime = LocalDateTime.of(
                Integer.parseInt(endDateSplits[0]),
                Integer.parseInt(endDateSplits[1]),
                Integer.parseInt(endDateSplits[2]),
                23,
                59);

        if(endTime.isBefore(startTime)){
            throw new Exception("종료 시간이 시작 시간보다 이전입니다. 올바른 시간을 입력하세요.");
        }

        String windFarmName = request.getWindFarmName();
        WindFarmOverview windFarmOverview = windFarmOverviewRepository.findFirstByName(windFarmName.toLowerCase());

        Integer windFarmId = windFarmOverview.getWindFarmId();
        Integer turbineId = request.getTurbineId();
        List<Memo> memoList;
        // 특정 turbine 데이터만 조회
        if(request.getDeviceType().equalsIgnoreCase("wind farm")){
            memoList = memoRepository.findByMemoIdWindFarmIdAndMemoIdTimestampBetween(windFarmId, startTime, endTime);
            System.out.println(memoList);
        }
        //wind farm 데이터 조회
        else if(request.getDeviceType().equalsIgnoreCase("wind turbine")){
            memoList = memoRepository.findByIdBetween(windFarmId, turbineId, startTime, endTime);
            System.out.println(memoList);
        }
        else {
            memoList = new ArrayList<>();
        }

        response = ReportsMapper.memoToReportsDTOResponse(memoList);

        /*
         * Save memo data to create excel
         * */
        reportDataInMemory.getReportDataMap().put(principal.getName(), response);

        return response;
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
                    Util.ifNullToEmptyString(memoData.getTimeStamp()),
                    Util.ifNullToEmptyString(memoData.getDeviceName()),
                    Util.ifNullToEmptyString(memoData.getEngineerName()),
                    Util.ifNullToEmptyString(memoData.getWorkTime()),
                    Util.ifNullToEmptyString(memoData.getMaterial()),
                    Util.ifNullToEmptyString(memoData.getQuantity()),
                    Util.ifNullToEmptyString(memoData.getWorkType()),
                    Util.ifNullToEmptyString(memoData.getInspection()),
                    Util.ifNullToEmptyString(memoData.getEtc())
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
                    Util.ifNullToEmptyString(dailyData.getDeviceName()),
                    Util.ifNullToEmptyString(dailyData.getDailyProduction()),
                    Util.ifNullToEmptyString(dailyData.getDailyAvailability()),
                    Util.ifNullToEmptyString(dailyData.getDailyCapacityFactor()),
                    Util.ifNullToEmptyString(dailyData.getMonthlyProduction()),
                    Util.ifNullToEmptyString(dailyData.getMonthlyCapacityFactor()),
                    Util.ifNullToEmptyString(dailyData.getMonthlyAvailability())
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

        Optional<AvailabilityType> optionalAvailabilityType = availabilityTypeRepository.findById(UUID.fromString("1c6ab584-ad0c-46a0-acaf-02a10abbe183"));
        AvailabilityType availabilityType = optionalAvailabilityType.orElseThrow(() ->  new Exception("Not matched total exported power name"));

        List<General> generalList = generalRepository.findAll();


        List<Long> dailyActualActivePowerList = new ArrayList<>();
        List<Long> monthlyActualActivePowerList = new ArrayList<>();

        for(General general : generalList)
        {
            long dailyActualActivePower;
            long monthlyActualActivePower;

            Optional<Long> dailyTotalPowerAtStartTime = availabilityDataRepository.getTimeAfterCertainTimestamp(general.getGeneralId().getTurbineId(), availabilityType.getUuid(), dailyStartTime);
            Optional<Long> monthlyTotalPowerAtStartTime = availabilityDataRepository.getTimeAfterCertainTimestamp(general.getGeneralId().getTurbineId(), availabilityType.getUuid(), monthlyStartTime);
            Optional<Long> totalPowerAtEndTime = availabilityDataRepository.getTimeBeforeCertainTimestamp(general.getGeneralId().getTurbineId(), availabilityType.getUuid(), endTime);

            if(totalPowerAtEndTime.isEmpty() || dailyTotalPowerAtStartTime.isEmpty())
                dailyActualActivePower = 0;
            else
                dailyActualActivePower = totalPowerAtEndTime.get() - dailyTotalPowerAtStartTime.get();

            if(totalPowerAtEndTime.isEmpty() || monthlyTotalPowerAtStartTime.isEmpty())
                monthlyActualActivePower = 0;
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
                .dailyProduction(String.valueOf(dailyActualActivePowerList.stream().mapToLong(Long::intValue).sum()))
                .dailyAvailability(String.valueOf(WindFarmService.Avail.average(dailyAvailabilityList)))
                .dailyCapacityFactor(String.valueOf(getTotalCapacityFactor(dailyStartTime, endTime, dailyActualActivePowerList)))
                .monthlyProduction(String.valueOf(monthlyActualActivePowerList.stream().mapToLong(Long::intValue).sum()))
                .monthlyAvailability(String.valueOf(WindFarmService.Avail.average(monthlyAvailabilityList)))
                .monthlyCapacityFactor(String.valueOf(getTotalCapacityFactor(monthlyStartTime, endTime, monthlyActualActivePowerList)))
                .build());



        List<Double> dailyCapacityFactorList = getCapacityFactor(dailyStartTime, endTime, dailyActualActivePowerList);
        List<Double> monthlyCapacityFactorList =  getCapacityFactor(monthlyStartTime, endTime, dailyActualActivePowerList);

        /*
        * Set each turbine data of table
        * */
        for(int turbineId = 0; turbineId < windFarmProperties.getTurbinesNumber(); turbineId++)
        {
            DailyReportDTO.Response.DailyData turbineDailyData = DailyReportDTO.Response.DailyData.builder()
                    .deviceName(String.format("WTG%02d", turbineId + 1))
                    .dailyProduction(String.valueOf(dailyActualActivePowerList.get(turbineId)))
                    .dailyAvailability(String.valueOf(dailyAvailabilityList.get(turbineId).getAvail()))
                    .dailyCapacityFactor(String.valueOf(dailyCapacityFactorList.get(turbineId)))
                    .monthlyProduction(String.valueOf(monthlyActualActivePowerList.get(turbineId)))
                    .monthlyAvailability(String.valueOf(monthlyAvailabilityList.get(turbineId).getAvail()))
                    .monthlyCapacityFactor(String.valueOf(monthlyCapacityFactorList.get(turbineId)))
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

    private List<Double> getCapacityFactor(LocalDateTime startTime, LocalDateTime endTime, List<Long> actualActivePowerList) throws Exception {
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
    private double getTotalCapacityFactor(LocalDateTime startTime, LocalDateTime endTime, List<Long> actualActivePowerList) throws Exception {
        List<General> generalList = generalRepository.findAll();
        long durationSeconds = Duration.between(startTime, endTime).toSeconds();

        double potentialActivePower = generalList.stream()
                .mapToDouble(value -> value.getRatedPower() * ((double) durationSeconds / 3600))
                .sum();

        double actualActivePower = actualActivePowerList.stream()
                .mapToLong(Long::intValue)
                .sum();

        return (actualActivePower / potentialActivePower) * 100;
    }
}
