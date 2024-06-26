import React from "react";
import styled from "styled-components";
import { Item, ItemHeader } from "./Common.styled";
import CheckBox from "@components/CheckBox";
import { StaticReportSelectionData } from "@pages/reports/StaticReport";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";

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

export type DeviceTypeSettingProps = {
  selectedDeviceType: string;
  selectedTurbine: string;
  selectedWindFarm: string;
};

type DeviceTypeProps = {
  onClick?: any;
  onChange?: any;
  props: StaticReportSelectionData;
};

export const initDeviceTypeSettingProps = (): DeviceTypeSettingProps => {
  return {
    selectedDeviceType: "Wind farm",
    selectedTurbine: "0",
    selectedWindFarm: "JEONG AM",
  };
};
const DeviceType = ({ onClick, onChange, props }: DeviceTypeProps) => {
  const turbineNumber = useSelector((store: RootState) => store.appReducer.turbineNumber);

  const createOptions = (turbineNumber: number) => {
    let newArr = [];
    for (let turbineId = 0; turbineId < turbineNumber; turbineId++)
      newArr.push(<option value={turbineId}>WTG{String(turbineId + 1).padStart(2, "0")}</option>);

    return newArr;
  };

  return (
    <StyledDeviceType>
      <ItemHeader>Device Type</ItemHeader>
      <WindFarmContainer>
        <CheckBox
          onChange={onChange}
          checked={props.selectedDeviceType === "Wind farm"}
          type="radio"
          text="Wind farm"
          width="150px"
          name="device-type"
        />
        <select onChange={onChange} name="wind-farm" id="wind-farm" disabled={props.selectedDeviceType !== "Wind farm"}>
          <option value="jeong-am">JEONG AM</option>
        </select>
      </WindFarmContainer>
      <WindTurbineContainer>
        <CheckBox
          onChange={onChange}
          checked={props.selectedDeviceType === "Wind turbine"}
          type="radio"
          text="Wind turbine"
          width="150px"
          name="device-type"
        />
        <select
          onChange={onChange}
          name="wind-turbine"
          id="wind-turbine"
          disabled={props.selectedDeviceType !== "Wind turbine"}
          value={props.selectedTurbine}>
          {createOptions(turbineNumber)}
        </select>
      </WindTurbineContainer>
    </StyledDeviceType>
  );
};

export default DeviceType;
