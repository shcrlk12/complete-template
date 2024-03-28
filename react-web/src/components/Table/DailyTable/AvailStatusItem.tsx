import React from "react";
import { StatusBox, StyledAvailStatusItem } from "./DailyTable.styled";

type AvailStatusItemProps = {
  status: string;
  color: string;
  time: number;
};
const AvailStatusItem = ({ status, color, time }: AvailStatusItemProps) => {
  return (
    <StyledAvailStatusItem>
      <StatusBox color={color}></StatusBox>
      <div>{status}</div>
      <div>
        <div>-</div>
        <div>{time}</div>
        <div>+</div>
      </div>
    </StyledAvailStatusItem>
  );
};

export default AvailStatusItem;
