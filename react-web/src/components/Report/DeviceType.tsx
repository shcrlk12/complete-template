import React from "react";
import styled from "styled-components";
import { Item, ItemHeader } from "./Common.styled";
import CheckBox from "@components/CheckBox";
import { StaticReportSelectionData } from "@pages/reports/StaticReport";

const StyledDeviceType = styled.div`
  padding: 10px 10px;
  border-bottom: ${({ theme }) => theme.colors.quaternary} solid 1px;
`;

const WindFarmContainer = styled(Item)`
  display: flex;
`;

const WindTurbineContainer = styled.div`
  display: flex;
`;

type DeviceTypeProps = {
  onClick?: any;
  onChange?: any;
  props: StaticReportSelectionData;
};

const DeviceType = ({ onClick, onChange, props }: DeviceTypeProps) => {
  return (
    <StyledDeviceType onChange={onChange}>
      <ItemHeader>Device Type</ItemHeader>
      <WindFarmContainer>
        <CheckBox
          checked={props.selectedDeviceType === "Wind farm"}
          type="radio"
          text="Wind farm"
          width="150px"
          name="device-type"
        />
        <select name="wind-farm" id="wind-farm" disabled={props.selectedDeviceType !== "Wind farm"}>
          <option value="jeong-am">JEONG AM</option>
        </select>
      </WindFarmContainer>
      <WindTurbineContainer>
        <CheckBox
          checked={props.selectedDeviceType === "Wind turbine"}
          type="radio"
          text="Wind turbine"
          width="150px"
          name="device-type"
        />
        <select
          name="wind-turbine"
          id="wind-turbine"
          disabled={props.selectedDeviceType !== "Wind turbine"}
          value={props.selectedTurbine}>
          <option value="0">WTG01</option>
          <option value="1">WTG02</option>
          <option value="2">WTG03</option>
        </select>
      </WindTurbineContainer>
    </StyledDeviceType>
  );
};

export default DeviceType;
