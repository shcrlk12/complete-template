package com.unison.scada.availability.api.reports;

import com.unison.scada.availability.api.memo.Memo;
import com.unison.scada.availability.api.memo.MemoRepository;
import com.unison.scada.availability.api.windfarm.WindFarmOverview;
import com.unison.scada.availability.api.windfarm.WindFarmOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportsService implements StaticReportService, MemoReportService{

    private final WindFarmOverviewRepository windFarmOverviewRepository;
    private final MemoRepository memoRepository;
    private final MemoReportDataInMemory memoReportDataInMemory;

    @Override
    public ReportsDTO.Response getStaticReportData(Principal principal,ReportsDTO.Request request) {
        return null;
    }

    @Override
    public MemoReportDTO.Response getMemoReportData(Principal principal, ReportsDTO.Request request) {
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
                0,
                0);


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
        memoReportDataInMemory.getMemoReportDataMap().put(principal.getName(), response);

        return response;
    }
}
