import React from "react";
import styled from "styled-components";
import { Item, ItemHeader } from "./Common.styled";
import CheckBox from "@components/CheckBox";

const StyledReportType = styled.div`
  padding: 10px 10px;
  border-bottom: ${({ theme }) => theme.colors.quaternary} solid 1px;
`;

type ReportTypeProps = {
  selectedValue: string;
  onClick?: any;
};

const ReportType = ({ selectedValue, onClick }: ReportTypeProps) => {
  return (
    <StyledReportType onClick={onClick}>
      <ItemHeader>Report Type</ItemHeader>
      <Item>
        <CheckBox checked={selectedValue === "Hourly"} type="radio" text="Hourly" width="150px" name="report-type" />
      </Item>
      <Item>
        <CheckBox checked={selectedValue === "Daily"} type="radio" text="Daily" width="150px" name="report-type" />
      </Item>
      <Item>
        <CheckBox checked={selectedValue === "Weekly"} type="radio" text="Weekly" width="150px" name="report-type" />
      </Item>
      <Item>
        <CheckBox checked={selectedValue === "Monthly"} type="radio" text="Monthly" width="150px" name="report-type" />
      </Item>
      <Item>
        <CheckBox checked={selectedValue === "Quarter"} type="radio" text="Quarter" width="150px" name="report-type" />
      </Item>
      <Item>
        <CheckBox
          checked={selectedValue === "Annually"}
          type="radio"
          text="Annually"
          width="150px"
          name="report-type"
        />
      </Item>
    </StyledReportType>
  );
};

export default ReportType;
