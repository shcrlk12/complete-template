import React, { ChangeEvent, MouseEvent, useState } from "react";
import {
  Header,
  ReportContainer,
  ReportInner,
  ReportTableContainer,
  RightHeaderContainer,
  TopOnTableButtonContainer,
} from "./Reports.styled";
import Button from "./../../components/Button/Button";
import { ButtonContainer } from "@pages/Common.styled";
import DeviceType, { DeviceTypeSettingProps, initDeviceTypeSettingProps } from "./../../components/Report/DeviceType";
import Period from "./../../components/Report/Period";
import ReportType from "./../../components/Report/ReportType";
import ReportTable, { ReportTableProps } from "@components/Report/Table/ReportTable";
import { backendServerIp } from "@src/Config";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import useFetchData from "@src/hooks/useFetchData";
import useDownloadFile from "@src/hooks/useDownloadFile";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";

export type StaticTableRow = {
  deviceName: string;
  time: string;
  windSpeed: string;
  energyProduction: string;
  availability: string;
  capacityFactor: string;
  forcedOutage: string;
};

export type StaticReportDataTableProps = {
  headerList: string[];
  tableData: StaticTableRow[];
};

export type StaticReportSelectionData = {
  selectedDeviceType: string;
  selectedTurbine: string;
};

const StaticReport = () => {
  const [deviceType, setDeviceType] = useState<DeviceTypeSettingProps>(initDeviceTypeSettingProps());
  const [startDate, setStartDate] = useState<Date>(new Date(Date.now()));
  const [endDate, setEndDate] = useState<Date>(new Date(Date.now()));
  const [reportType, setReportType] = useState<string>("Hourly");
  const [staticTableData, setStaticTableData] = useState<ReportTableProps | null>(null);

  const fetchData = useFetchData();
  const downloadFile = useDownloadFile();

  const fetchReportTableData = async () => {
    const data = await fetchData<ReportTableProps>(
      `http://${backendServerIp}/api/reports/static?startDate=${startDate.getFullYear()}_${startDate.getMonth() + 1}_${startDate.getDate()}&endDate=${endDate.getFullYear()}_${endDate.getMonth() + 1}_${endDate.getDate()}&deviceType=${deviceType.selectedDeviceType}&windFarmName=${deviceType.selectedWindFarm}&turbineId=${deviceType.selectedTurbine}&reportType=${reportType}`,
      {
        mode: "cors",
        method: "GET",
        credentials: "include",
      },
    );
    setStaticTableData(data);
    console.log(data);
  };

  const downloadExcel = () => {
    downloadFile(`http://${backendServerIp}/api/reports/static/download/excel`, {
      mode: "cors",
      method: "GET",
      credentials: "include",
    });
  };

  const changeDeviceType = (event: ChangeEvent<HTMLSelectElement | HTMLInputElement>) => {
    if (event.target instanceof HTMLInputElement) {
      setDeviceType((current) => ({
        ...current,
        selectedDeviceType: event.target.value,
      }));
    } else if (event.target instanceof HTMLSelectElement) {
      setDeviceType((current) => ({
        ...current,
        selectedTurbine: event.target.value,
      }));
    }
  };

  return (
    <ReportContainer>
      {staticTableData === null ? (
        <ReportInner>
          <Header>Static Report</Header>
          <DeviceType props={deviceType} onChange={changeDeviceType} />
          <Period
            startDate={startDate}
            endDate={endDate}
            onChange={(event: ChangeEvent<HTMLSelectElement>) => {
              let target = event.target;

              if (target instanceof HTMLSelectElement) {
                if (event.target.name.includes("start")) {
                  let newDate = new Date(startDate.getTime());
                  if (event.target.name === "start-date-year") {
                    newDate.setFullYear(Number(event.target.value));
                  } else if (event.target.name === "start-date-month") {
                    newDate.setMonth(Number(event.target.value) - 1);
                  } else if (event.target.name === "start-date-date") {
                    newDate.setDate(Number(event.target.value));
                  }
                  setStartDate(newDate);
                } else if (event.target.name.includes("end")) {
                  let newDate = new Date(endDate.getTime());
                  if (event.target.name === "end-date-year") {
                    newDate.setFullYear(Number(event.target.value));
                  } else if (event.target.name === "end-date-month") {
                    newDate.setMonth(Number(event.target.value) - 1);
                  } else if (event.target.name === "end-date-date") {
                    newDate.setDate(Number(event.target.value));
                  }
                  setEndDate(newDate);
                }
              }
            }}
          />
          <ReportType
            selectedValue={reportType}
            onClick={(event: MouseEvent<HTMLInputElement>) => {
              if (event.target instanceof HTMLInputElement) {
                setReportType(event.target.value);
              }
            }}
          />
          <ButtonContainer>
            <Button type="submit" isPrimary={true} text="Craet data" width="100%" onClick={fetchReportTableData} />
          </ButtonContainer>
        </ReportInner>
      ) : (
        <ReportTableContainer>
          <TopOnTableButtonContainer>
            <Button
              text={
                <>
                  <ArrowBackIcon />
                </>
              }
              width="30px"
              height="25px"
              radius="5px"
              padding="0px"
              onClick={() => {
                setStaticTableData(null);
              }}
            />
            <RightHeaderContainer>
              <Button text="Download" height="25px" radius="5px" onClick={downloadExcel} />
            </RightHeaderContainer>
          </TopOnTableButtonContainer>
          <ReportTable tableHeader={staticTableData.tableHeader} tableData={staticTableData.tableData} />
        </ReportTableContainer>
      )}
    </ReportContainer>
  );
};

export default StaticReport;
