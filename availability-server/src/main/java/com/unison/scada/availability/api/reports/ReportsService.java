package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.windfarm.WindFarmOverview;
import com.unison.scada.availability.api.windfarm.WindFarmOverviewRepository;
import com.unison.scada.availability.global.ReportExcelGenerator;
import com.unison.scada.availability.global.Util;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportsService implements StaticReportService, MemoReportService, MemoReportGenerateService{

    private final WindFarmOverviewRepository windFarmOverviewRepository;
    private final MemoRepository memoRepository;
    private final MemoReportDataInMemory memoReportDataInMemory;

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

        // save search memo data to create a Excel file
        memoReportDataInMemory.getMemoReportDataMap().put(principal.getName(), response);

        return response;
    }

    @Override
    public void memoReportExcelGenerate(HttpServletResponse response, Principal principal) throws IOException {
        ReportExcelGenerator reportExcelGenerator = new ReportExcelGenerator();

        reportExcelGenerator.setHeaderCellBackgroundColor(new XSSFColor(new byte[]{(byte) 66, (byte) 125, (byte) 158}));

        /*
        * make header and body datas
        * */

        MemoReportDTO.Response memoDto = memoReportDataInMemory.getMemoReportDataMap().get(principal.getName());
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

        String fileName = "spring_excel_download";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
