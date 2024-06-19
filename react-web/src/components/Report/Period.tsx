import React, { useState } from "react";
import styled from "styled-components";
import { Item, ItemHeader } from "./Common.styled";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";

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

const ToContainer = styled.div`
  display: flex;
`;

type PeriodProps = {
  startDate: Date;
  endDate: Date;
  onChange?: any;
};
const Period = ({ startDate, endDate, onChange }: PeriodProps) => {
  const { startOfWarrantyDate } = useSelector((store: RootState) => store.appReducer);
  const now = new Date(Date.now());

  const startWarrantyDate = new Date(startOfWarrantyDate);

  const createDateOpsionSinceStartDate = (date: Date, type: string) => {
    const options = [];
    if (type === "year") {
      for (let time = startWarrantyDate.getFullYear(); time <= now.getFullYear(); time++)
        options.push(<option value={time}>{time}</option>);
    } else if (type === "month") {
      for (let time = 1; time <= 12; time++) options.push(<option value={time}>{time}</option>);
    } else if (type === "date") {
      const lastDate = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
      for (let time = 1; time <= lastDate; time++) options.push(<option value={time}>{time}</option>);
    }

    return options;
  };
  return (
    <StyledPeriods>
      <ItemHeader>Periods</ItemHeader>
      <FromContainer>
        <PeriodDateText>From:</PeriodDateText>
        <select onChange={onChange} value={startDate.getFullYear()} name="start-date-year" id="start-date-year">
          {createDateOpsionSinceStartDate(startDate, "year")}
        </select>
        -
        <select onChange={onChange} value={startDate.getMonth() + 1} name="start-date-month" id="start-date-month">
          {createDateOpsionSinceStartDate(startDate, "month")}
        </select>
        -
        <select onChange={onChange} value={startDate.getDate()} name="start-date-date" id="start-date-date">
          {createDateOpsionSinceStartDate(startDate, "date")}
        </select>
      </FromContainer>
      <ToContainer>
        <PeriodDateText>To:</PeriodDateText>
        <select onChange={onChange} value={endDate.getFullYear()} name="end-date-year" id="end-date-year">
          {createDateOpsionSinceStartDate(endDate, "year")}
        </select>
        -
        <select onChange={onChange} value={endDate.getMonth() + 1} name="end-date-month" id="end-date-month">
          {createDateOpsionSinceStartDate(endDate, "month")}
        </select>
        -
        <select onChange={onChange} value={endDate.getDate()} name="end-date-date" id="end-date-date">
          {createDateOpsionSinceStartDate(endDate, "date")}
        </select>
      </ToContainer>
    </StyledPeriods>
  );
};

export default Period;
