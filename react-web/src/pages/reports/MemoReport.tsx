import React, { ChangeEvent, useState } from "react";
import {
  Header,
  ReportContainer,
  ReportInner,
  ReportTableContainer,
  TopOnTableButtonContainer,
} from "./Reports.styled";
import Button from "./../../components/Button/Button";
import { ButtonContainer } from "@pages/Common.styled";
import DeviceType from "./../../components/Report/DeviceType";
import Period from "./../../components/Report/Period";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { backendServerIp } from "@src/Config";
import ReportTable, { ReportTableProps, initReportTableProps } from "@components/Report/Table/ReportTable";
import { current } from "@reduxjs/toolkit";
import { isIsoDateString } from "@src/util/date";
import styled from "styled-components";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { convertJsonToTableProps } from "./Report";
import useDownloadFile from "@src/hooks/useDownloadFile";

export type MemoTableRow = {
  deviceName: string;
  timeStamp: string;
  engineerName: string;
  workTime: string;
  material: string;
  quantity: string;
  workType: string;
  inspection: string;
  etc: string;
};

export type MemoReportDataTableProps = {
  headerList: string[];
  tableData: MemoTableRow[];
};

export type MemoReportSelectionData = {
  selectedDeviceType: string;
  selectedTurbine: string;
  selectedWindFarm: string;
};

const MemoReport = () => {
  const { dispatch, navigate } = useInits();
  const [deviceType, setDeviceType] = useState<MemoReportSelectionData>({
    selectedDeviceType: "Wind farm",
    selectedTurbine: "0",
    selectedWindFarm: "JEONG AM",
  });

  const [startDate, setStartDate] = useState<Date>(new Date(Date.now()));
  const [endDate, setEndDate] = useState<Date>(new Date(Date.now()));
  const [tableData, setTableData] = useState<ReportTableProps | null>(null);
  const downloadFile = useDownloadFile();

  const fetchReportTableData = () => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch(
        `http://${backendServerIp}/api/reports/memo?startDate=${startDate.getFullYear()}_${startDate.getMonth() + 1}_${startDate.getDate()}&endDate=${endDate.getFullYear()}_${endDate.getMonth() + 1}_${endDate.getDate()}&deviceType=${deviceType.selectedDeviceType}&windFarmName=${deviceType.selectedWindFarm}&turbineId=${deviceType.selectedTurbine}`,
        {
          mode: "cors",
          method: "GET",
          credentials: "include",
        },
      );

      await statusOk(response);

      const json = await response.json();
      const data: MemoReportDataTableProps = json.data;

      setTableData(convertJsonToTableProps(data));
    });
  };

  const downloadExcel = () => {
    downloadFile(`http://${backendServerIp}/api/reports/memo/download/excel`, {
      mode: "cors",
      method: "GET",
      credentials: "include",
    });
  };

  const changeMemoReportDate = (event: ChangeEvent<HTMLSelectElement>) => {
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
    <>
      {tableData === null ? (
        <ReportContainer>
          <ReportInner>
            <Header>Memo Report</Header>
            <DeviceType props={deviceType} onChange={changeDeviceType} />
            <Period startDate={startDate} endDate={endDate} onChange={changeMemoReportDate} />
            <ButtonContainer>
              <Button type="submit" isPrimary={true} text="Craet data" width="100%" onClick={fetchReportTableData} />
            </ButtonContainer>
          </ReportInner>
        </ReportContainer>
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
                setTableData(null);
              }}
            />
            <Button text="Download" height="25px" radius="5px" onClick={downloadExcel} />
          </TopOnTableButtonContainer>
          <ReportTable tableHeader={tableData.tableHeader} tableData={tableData.tableData} />
        </ReportTableContainer>
      )}
    </>
  );
};

export default MemoReport;
