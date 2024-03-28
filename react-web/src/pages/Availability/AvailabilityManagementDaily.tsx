import React from "react";
import { AvailabilityTableContainer, MainSection } from "./Availability.styled";
import DailyTable from "@components/Table/DailyTable/DailyTable";

const AvailabilityManagementDaily = () => {
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
