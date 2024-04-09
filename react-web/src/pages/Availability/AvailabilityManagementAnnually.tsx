import React, { useEffect } from "react";
import PowerCondition from "@components/PowerCondition";
import AnnualTable from "@components/Table/AnnualTable/AnnualTable";
import {
  AvailabilityTableContainer,
  MainSection,
  PowerStatusList,
  TableMetaContainer,
  WindfarmInfoList,
} from "./Availability.styled";
import { useDispatch } from "react-redux";
import { resetLoading } from "@reducers/appAction";

const AvailabilityManagementAnnually = () => {
  const dispatch = useDispatch();

  useEffect(() => {}, []);

  return (
    <MainSection>
      <TableMetaContainer>
        <WindfarmInfoList>
          <li>
            <strong>20XX년 가동률</strong>
            <div>
              <strong>99.8</strong> %
            </div>
          </li>
          <li>
            <strong>Wind speed</strong>
            <div>
              <strong>15.8</strong> m/s
            </div>
          </li>
          <li>
            <strong>Active power</strong>
            <div>
              <strong>84,120.25</strong> kw
            </div>
          </li>
        </WindfarmInfoList>
        <PowerStatusList>
          <PowerCondition cubeColor="#339D33" width="162px" text="95% 이상" />
          <PowerCondition cubeColor="#D9D633" width="162px" text="95% 이하" />
          <PowerCondition cubeColor="#D97733" width="162px" text="90% 이하" />
          <PowerCondition cubeColor="#D93333" width="162px" text="30% 이하" />
        </PowerStatusList>
      </TableMetaContainer>
      <AvailabilityTableContainer>
        <AnnualTable />
      </AvailabilityTableContainer>
    </MainSection>
  );
};

export default AvailabilityManagementAnnually;
