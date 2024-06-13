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

  const getDateOpeions = (date: Date, type: string) => {
    const options = [];
    if (type === "year") {
      for (let time = startOfWarrantyDate.getFullYear(); time <= now.getFullYear(); time++)
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
    <StyledPeriods onChange={onChange}>
      <ItemHeader>Periods</ItemHeader>
      <FromContainer>
        <PeriodDateText>From:</PeriodDateText>
        <select value={startDate.getFullYear()} name="start-date-year" id="start-date-year">
          {getDateOpeions(startDate, "year")}
        </select>
        -
        <select value={startDate.getMonth() + 1} name="start-date-month" id="start-date-month">
          {getDateOpeions(startDate, "month")}
        </select>
        -
        <select value={startDate.getDate()} name="start-date-date" id="start-date-date">
          {getDateOpeions(startDate, "date")}
        </select>
      </FromContainer>
      <ToContainer>
        <PeriodDateText>To:</PeriodDateText>
        <select value={endDate.getFullYear()} name="end-date-year" id="end-date-year">
          {getDateOpeions(endDate, "year")}
        </select>
        -
        <select value={endDate.getMonth() + 1} name="end-date-month" id="end-date-month">
          {getDateOpeions(endDate, "month")}
        </select>
        -
        <select value={endDate.getDate()} name="end-date-date" id="end-date-date">
          {getDateOpeions(endDate, "date")}
        </select>
      </ToContainer>
    </StyledPeriods>
  );
};

export default Period;
