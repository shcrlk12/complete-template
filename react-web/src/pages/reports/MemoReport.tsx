import React, { ChangeEvent, useState } from "react";
import { Header, ReportContainer, ReportInner } from "./Reports.styled";
import Button from "./../../components/Button/Button";
import { ButtonContainer } from "@pages/Common.styled";
import DeviceType from "./../../components/Report/DeviceType";
import Period from "./../../components/Report/Period";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { backendServerIp } from "@src/Config";
import ReportTable, { ReportTableProps } from "@components/Report/Table/ReportTable";
import { current } from "@reduxjs/toolkit";
import { isIsoDateString } from "@src/util/date";
import styled from "styled-components";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

type TableRow = {
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
  tableData: TableRow[];
};

export type MemoReportSelectionData = {
  selectedDeviceType: string;
  selectedTurbine: string;
  selectedWindFarm: string;
};

const TopOnTableButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;

  margin-bottom: 10px;
`;

const ReportTableContainer = styled.div`
  padding: 10px;
`;

const MemoReport = () => {
  const { dispatch, navigate } = useInits();
  const [deviceType, setDeviceType] = useState<MemoReportSelectionData>({
    selectedDeviceType: "Wind farm",
    selectedTurbine: "0",
    selectedWindFarm: "JEONG AM",
  });

  const [startDate, setStartDate] = useState<Date>(new Date(Date.now()));
  const [endDate, setEndDate] = useState<Date>(new Date(Date.now()));
  const [staticTableData, setStaticTableData] = useState<ReportTableProps | null>(null);

  const createData = () => {
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

      let tableProps: ReportTableProps = { tableHeader: [], tableData: { row: [] } };

      data.headerList.forEach((value) => tableProps.tableHeader.push({ name: value, unit: null }));

      data.tableData.forEach((data) => {
        let value: string[] = [];
        let key: keyof TableRow;

        for (key in data) {
          value.push(data[key]);
        }
        tableProps.tableData.row.push({ value: value });
      });

      setStaticTableData(tableProps);
    });
  };
  return (
    <>
      {staticTableData === null ? (
        <ReportContainer>
          <ReportInner>
            <Header>Memo Report</Header>
            <DeviceType
              props={deviceType}
              onChange={(event: ChangeEvent<HTMLSelectElement>) => {
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
              }}
            />
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
            <ButtonContainer>
              <Button type="submit" isPrimary={true} text="Craet data" width="100%" onClick={createData} />
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
                setStaticTableData(null);
              }}
            />
            <Button
              text="Download"
              height="25px"
              radius="5px"
              onClick={() => {
                fetchData(dispatch, navigate, async () => {
                  const response = await fetch(`http://${backendServerIp}/api/reports/memo/download/excel`, {
                    mode: "cors",
                    method: "GET",
                    credentials: "include",
                  });

                  const blob = await response.blob();
                  const url = window.URL.createObjectURL(blob);
                  const link = document.createElement("a");
                  link.href = url;
                  link.download = "memo-report.xlsx";
                  document.body.appendChild(link);
                  link.click();
                  document.body.removeChild(link);
                  window.URL.revokeObjectURL(url);
                });
              }}
            />
          </TopOnTableButtonContainer>
          <ReportTable tableHeader={staticTableData.tableHeader} tableData={staticTableData.tableData} />
        </ReportTableContainer>
      )}
    </>
  );
};

export default MemoReport;
