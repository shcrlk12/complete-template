import React, { useEffect } from "react";
import { AvailabilityTableContainer, MainSection } from "./Availability.styled";
import DailyTable from "@components/Table/DailyTable/DailyTable";
import { initPage } from "@src/App";
import { useDispatch } from "react-redux";

const AvailabilityManagementDaily = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    initPage(dispatch);
  }, []);

  return (
    <>
      <MainSection>
        <AvailabilityTableContainer>
          <DailyTable />
        </AvailabilityTableContainer>
      </MainSection>
    </>
  );
};

export default AvailabilityManagementDaily;
