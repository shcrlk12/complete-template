import React, { useEffect, useState } from "react";
import PowerCondition from "@components/PowerCondition";
import AnnualTable from "@components/Table/AnnualTable/AnnualTable";
import {
  AvailabilityTableContainer,
  MainSection,
  PowerStatusList,
  TableMetaContainer,
  WindfarmInfoList,
} from "./Availability.styled";
import { useDispatch } from "react-redux";
import { resetLoading, setStartOfWarrantyDate } from "@reducers/appAction";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { loginSuccess } from "@reducers/userActions";
import { useParams } from "react-router";
import { backendServerIp, windSpeedName } from "@src/Config";

type DataOfDay = {
  time: Date;
  availability: number;
};
type AnnuallyTurbineData = {
  turbineId: number;
  availability: number;
  data: DataOfDay[];
};

export type AnnuallyTableData = {
  turbinesNumber: number;
  availability: number;
  capacityFactor: number;
  yearsOfWarranty: number;
  startTimeOfYears: Date;
  date: string;
  turbines: AnnuallyTurbineData[];
};

type RealTimeDataType = {
  base: number;
  name: string;
  value: number;
};

type RealTimeType = {
  timestamp: Date;
  dataList: RealTimeDataType[];
};

const AvailabilityManagementAnnually = () => {
  const { dispatch, navigate } = useInits();
  const { year } = useParams();

  const [annuallyTableData, setAnnuallyTableData] = useState<AnnuallyTableData>();
  const [realTime, setRealTime] = useState<RealTimeType>();

  useEffect(() => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch(`http://${backendServerIp}/api/wind-farm/annually/${year}`, {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      await statusOk(response);

      const json = await response.json();
      const data = json.data;

      console.log(data);

      let startDate = new Date(data.startTimeOfYears);
      startDate.setFullYear(startDate.getFullYear() - (data.yearsOfWarranty - 1));

      dispatch(setStartOfWarrantyDate(startDate));

      setAnnuallyTableData(data);
    });

    const fetchRealTimeData = async () => {
      const response = await fetch(`http://${backendServerIp}/api/wind-farm/realtime`, {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      const json = await response.json();
      const data = json.data;

      setRealTime(data);
    };
    fetchRealTimeData();

    const intervalId = setInterval(fetchRealTimeData, 2000);
    return () => clearInterval(intervalId);
  }, [year]);

  const getPeriod = (date: Date): string => {
    let startDate = new Date(date);
    let endDate = new Date(startDate); //1년후 시간.

    endDate.setFullYear(endDate.getFullYear() + 1);
    endDate.setDate(endDate.getDate() - 1);
    if (endDate.getTime() > Date.now()) {
      endDate = new Date(Date.now());
    }
    return `${startDate.getFullYear()}.${startDate.getMonth() + 1}.${startDate.getDate()} - ${endDate.getFullYear()}.${endDate.getMonth() + 1}.${endDate.getDate()}`;
  };

  const getWindSpeed = () => {
    if (realTime !== undefined) {
      for (const data of realTime.dataList) {
        if (data.name === windSpeedName) return (data.value / data.base).toFixed(1);
      }
    }
  };

  return (
    <MainSection>
      <TableMetaContainer>
        <WindfarmInfoList>
          <li>
            <strong>기간</strong>
            <div>
              <strong> {annuallyTableData && getPeriod(annuallyTableData.startTimeOfYears)}</strong>
            </div>
          </li>
          <li>
            <strong>가동률</strong>
            <div>
              <strong>{annuallyTableData?.availability.toFixed(2)}</strong> %
            </div>
          </li>
          <li>
            <strong>이용률</strong>
            <div>
              <strong>{annuallyTableData?.capacityFactor.toFixed(2)}</strong> %
            </div>
          </li>
          <li>
            <strong>풍속</strong>
            <div>
              <strong>{realTime && getWindSpeed()}</strong> m/s
            </div>
          </li>
        </WindfarmInfoList>
        <PowerStatusList>
          <PowerCondition cubeColor="#339D33" width="162px" text="95% 이상" />
          <PowerCondition cubeColor="#D9D633" width="162px" text="95% 이하" />
          <PowerCondition cubeColor="#D97733" width="162px" text="90% 이하" />
          <PowerCondition cubeColor="#D93333" width="162px" text="30% 이하" />
        </PowerStatusList>
      </TableMetaContainer>
      <AvailabilityTableContainer>
        {annuallyTableData ? <AnnualTable annuallyTableData={annuallyTableData} /> : null}
      </AvailabilityTableContainer>
    </MainSection>
  );
};

export default AvailabilityManagementAnnually;
