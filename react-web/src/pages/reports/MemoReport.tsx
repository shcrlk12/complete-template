import React, { ChangeEvent, useState } from "react";
import { Header, ReportContainer, ReportInner } from "./Reports.styled";
import Button from "./../../components/Button/Button";
import { ButtonContainer } from "@pages/Common.styled";
import DeviceType from "./../../components/Report/DeviceType";
import Period from "./../../components/Report/Period";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { backendServerIp } from "@src/Config";
import StaticReportDataTable, { StaticReportDataTableProps } from "@components/Report/Table/StaticReportDataTable";
import { current } from "@reduxjs/toolkit";

export type MemoReportSelectionData = {
  selectedDeviceType: string;
  selectedTurbine: string;
  selectedWindFarm: string;
};

const MemoReport = () => {
  const { dispatch, navigate } = useInits();
  const [deviceType, setDeviceType] = useState<MemoReportSelectionData>({
    selectedDeviceType: "Wind farm",
    selectedTurbine: "1",
    selectedWindFarm: "JEONG AM",
  });

  const [startDate, setStartDate] = useState<Date>(new Date(Date.now()));
  const [endDate, setEndDate] = useState<Date>(new Date(Date.now()));
  const [staticTableData, setStaticTableData] = useState<StaticReportDataTableProps[]>();

  const createMemoData = (): StaticReportDataTableProps => {
    let newArr: StaticReportDataTableProps = {
      tableHeader: [
        { name: "Device Name", unit: null },
        { name: "Time", unit: null },
        { name: "작업자", unit: null },
        { name: "시간", unit: null },
        { name: "자재", unit: null },
        { name: "수량", unit: null },
        { name: "작업 유형", unit: null },
        { name: "점검 내역", unit: null },
        { name: "기타(비고)", unit: null },
      ],
      tableData: {
        row: [
          {
            value: [
              "WTG01",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG02",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG03",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG04",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG05",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG06",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG07",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG08",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG09",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG10",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG11",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG12",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
          {
            value: [
              "WTG13",
              "2024-11-05",
              "홍길동",
              "2024-11-05 11:30",
              "모터",
              "3개",
              "어쩌고 저쩌고",
              "어쩌고 저쩌고2",
              "어쩌고 저쩌고3",
            ],
          },
        ],
      },
    };
    return newArr;
  };

  const { tableHeader, tableData } = createMemoData();

  const createData = () => {
    setStaticTableData([]);
    fetchData(dispatch, navigate, async () => {
      const response = await fetch(
        `http://${backendServerIp}/api/reports/memo?startDate=${startDate.getFullYear()}_${startDate.getMonth() + 1}_${startDate.getDate()}&endDate=${endDate.getFullYear()}_${endDate.getMonth() + 1}_${endDate.getDate()}&windFarmName=${deviceType.selectedWindFarm}&turbineId=${deviceType.selectedTurbine}`,
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
      ) : (
        <StaticReportDataTable tableHeader={tableHeader} tableData={tableData} />
      )}
    </ReportContainer>
  );
};

export default MemoReport;
