import React, { ChangeEvent, useState } from "react";
import {
  DateContainer,
  Header,
  ReportContainer,
  ReportInner,
  ReportTableContainer,
  RightHeaderContainer,
  TopOnTableButtonContainer,
} from "./Reports.styled";
import { ButtonContainer } from "@pages/Common.styled";
import Button from "@components/Button/Button";
import { Item, ItemHeader } from "@components/Report/Common.styled";
import styled from "styled-components";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";
import ReportTable, { ReportTableProps } from "@components/Report/Table/ReportTable";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { backendServerIp } from "@src/Config";
import { convertJsonToTableProps, createDateOpsionSinceStartDate } from "./Report";
import useFetchData from "./../../hooks/useFetchData";
import useDownloadFile from "./../../hooks/useDownloadFile";

export type DailyTableRow = {
  deviceName: string;
  dailyProduction: string;
  dailyAvailability: string;
  dailyCapacityFactor: string;
  monthlyProduction: string;
  monthlyAvailability: string;
  monthlyCapacityFactor: string;
};

export type DailyReportDataTableProps = {
  headerList: string[];
  tableData: DailyTableRow[];
};

const StyledPeriods = styled.div`
  padding: 10px 10px;
  border-bottom: ${({ theme }) => theme.colors.quaternary} solid 1px;
`;

const PeriodDateText = styled.div`
  width: 80px;
`;

const FromContainer = styled(Item)`
  display: flex;
`;

const DailyReport = () => {
  const { startOfWarrantyDate } = useSelector((store: RootState) => store.appReducer);
  const [dailyReportDate, setDailyReportDate] = useState<Date>(new Date(Date.now()));
  const [tableData, setTableData] = useState<ReportTableProps | null>(null);
  const fetchData = useFetchData();
  const downloadFile = useDownloadFile();
  const startWarrantyDate = new Date(startOfWarrantyDate);

  const fetchReportTableData = async () => {
    const data = await fetchData<DailyReportDataTableProps>(
      `http://${backendServerIp}/api/reports/daily?dailyDate=${dailyReportDate.getFullYear()}_${dailyReportDate.getMonth() + 1}_${dailyReportDate.getDate()}`,
      {
        mode: "cors",
        method: "GET",
        credentials: "include",
      },
    );
    setTableData(convertJsonToTableProps(data));
    console.log(data);
  };

  const downloadExcel = () => {
    downloadFile(`http://${backendServerIp}/api/reports/daily/download/excel`, {
      mode: "cors",
      method: "GET",
      credentials: "include",
    });
  };

  const changeDailyReportDate = (event: ChangeEvent<HTMLSelectElement>) => {
    let target = event.target;
    let newDate = new Date(dailyReportDate.getTime());

    if (target.name === "daily-date-year") {
      newDate.setFullYear(Number(target.value));
    } else if (target.name === "daily-date-month") {
      newDate.setMonth(Number(target.value) - 1);
    } else if (target.name === "daily-date-date") {
      newDate.setDate(Number(target.value));
    }
    setDailyReportDate(newDate);
  };
  return (
    <>
      {tableData === null ? (
        <ReportContainer>
          <ReportInner>
            <Header>Daily Report</Header>
            <StyledPeriods>
              <ItemHeader>Periods</ItemHeader>
              <FromContainer>
                <PeriodDateText>From:</PeriodDateText>
                <select
                  onChange={changeDailyReportDate}
                  value={dailyReportDate.getFullYear()}
                  name="daily-date-year"
                  id="daily-date-year">
                  {createDateOpsionSinceStartDate(startWarrantyDate, dailyReportDate, "year")}
                </select>
                -
                <select
                  onChange={changeDailyReportDate}
                  value={dailyReportDate.getMonth() + 1}
                  name="daily-date-month"
                  id="daily-date-month">
                  {createDateOpsionSinceStartDate(startWarrantyDate, dailyReportDate, "month")}
                </select>
                -
                <select
                  onChange={changeDailyReportDate}
                  value={dailyReportDate.getDate()}
                  name="daily-date-date"
                  id="daily-date-date">
                  {createDateOpsionSinceStartDate(startWarrantyDate, dailyReportDate, "date")}
                </select>
              </FromContainer>
            </StyledPeriods>
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
            <RightHeaderContainer>
              <DateContainer>{`${dailyReportDate.getFullYear()}-${dailyReportDate.getMonth() + 1}-${dailyReportDate.getDate()}`}</DateContainer>
              <Button text="Download" height="25px" radius="5px" onClick={downloadExcel} />
            </RightHeaderContainer>
          </TopOnTableButtonContainer>
          <ReportTable tableHeader={tableData.tableHeader} tableData={tableData.tableData} />
        </ReportTableContainer>
      )}
    </>
  );
};

export default DailyReport;
