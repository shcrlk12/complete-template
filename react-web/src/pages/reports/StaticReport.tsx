import useInits from "@src/hooks/useInits";
import React, { ChangeEvent, MouseEvent, useEffect, useState } from "react";
import { Header, ReportContainer, ReportInner } from "./Reports.styled";
import Button from "./../../components/Button/Button";
import { ButtonContainer } from "@pages/Common.styled";
import DeviceType from "./../../components/Report/DeviceType";
import Period from "./../../components/Report/Period";
import ReportType from "./../../components/Report/ReportType";
import StaticReportDataTable, { StaticReportDataTableProps } from "@components/Report/Table/StaticReportDataTable";
import { backendServerIp } from "@src/Config";
import { fetchData, statusOk } from "@src/util/fetch";

export type StaticReportSelectionData = {
  selectedDeviceType: string;
  selectedTurbine: string;
};

const StaticReport = () => {
  const { dispatch, navigate } = useInits();
  const [deviceType, setDeviceType] = useState<StaticReportSelectionData>({
    selectedDeviceType: "Wind farm",
    selectedTurbine: "WTG01",
  });
  const [startDate, setStartDate] = useState<Date>(new Date(Date.now()));
  const [endDate, setEndDate] = useState<Date>(new Date(Date.now()));
  const [reportType, setReportType] = useState<string>("Hourly");
  const [staticTableData, setStaticTableData] = useState<StaticReportDataTableProps[]>();

  const createStaticData = (): StaticReportDataTableProps => {
    let newArr: StaticReportDataTableProps = {
      tableHeader: [
        { name: "Device Name", unit: null },
        { name: "Time", unit: null },
        { name: "Wind Speed", unit: "m/s" },
        { name: "Energy Production", unit: "Mwh" },
        { name: "Availability", unit: "%" },
        { name: "Capacity Factor", unit: "%" },
        { name: "Forced Outage", unit: "min" },
        { name: "Scheduled Maintenance", unit: "min" },
        { name: "Requested Shutdown", unit: "min" },
        { name: "Normal Status", unit: "min" },
        { name: "Information Unavailable", unit: "min" },
      ],
      tableData: {
        row: [
          { value: ["WTG01", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG02", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG03", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG04", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG05", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG06", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG07", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG08", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG09", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG10", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG11", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
          { value: ["WTG12", "2024-11-05", "10.5", "81153", "66.8", "23.8", "500", "400", "300", "200", "100"] },
        ],
      },
    };
    return newArr;
  };

  const { tableHeader, tableData } = createStaticData();
  const createData = () => {
    setStaticTableData([]);

    fetchData(dispatch, navigate, async () => {
      const response = await fetch(
        `http://${backendServerIp}/api/reports/static?startDate=${startDate}&endDate=${endDate}`,
        {
          mode: "cors",
          method: "GET",
          credentials: "include",
        },
      );

      await statusOk(response);

      const json = await response.json();
    });
  };
  return (
    <ReportContainer>
      {staticTableData === undefined ? (
        <ReportInner>
          <Header>Static Report</Header>
          <DeviceType
            props={deviceType}
            onChange={(event: ChangeEvent<HTMLSelectElement>) => {
              if (event.target instanceof HTMLInputElement) {
                setDeviceType({
                  ...deviceType,
                  selectedDeviceType: event.target.value,
                });
              } else if (event.target instanceof HTMLSelectElement) {
                setDeviceType({
                  ...deviceType,
                  selectedTurbine: event.target.value,
                });
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
          <ReportType
            selectedValue={reportType}
            onClick={(event: MouseEvent<HTMLInputElement>) => {
              if (event.target instanceof HTMLInputElement) {
                setReportType(event.target.value);
              }
            }}
          />
          <ButtonContainer>
            <Button type="submit" isPrimary={true} text="Craet data" width="100%" onClick={createData} />
          </ButtonContainer>
        </ReportInner>
      ) : (
        <StaticReportDataTable tableHeader={tableHeader} tableData={tableData} />
      )}
    </ReportContainer>
  );
};

export default StaticReport;
