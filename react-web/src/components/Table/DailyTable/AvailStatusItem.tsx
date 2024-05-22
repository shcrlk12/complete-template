import React from "react";
import { StatusBox, StyledAvailStatusItem } from "./DailyTable.styled";
import Button from "./../../Button/Button";
import styled from "styled-components";

type AvailStatusItemProps = {
  status: string;
  color: string;
  time: number;
  onClick?: any;
};

const StyledButton = styled(Button)`
  padding: 0;
  height: 20px;
  width: 20px;
`;

export const STATUS_CLASS_NAME = "status-name";
export const PLUS_BUTTON = "+";
export const MINUS_BUTTON = "-";

const AvailStatusItem = ({ status, color, time, onClick }: AvailStatusItemProps) => {
  return (
    <StyledAvailStatusItem onClick={onClick}>
      <StatusBox color={color}></StatusBox>
      <div className={STATUS_CLASS_NAME}>{status}</div>
      <div>
        <StyledButton text={MINUS_BUTTON} />
        <div>{time}</div>
        <StyledButton text={PLUS_BUTTON} />
      </div>
    </StyledAvailStatusItem>
  );
};

export default AvailStatusItem;
