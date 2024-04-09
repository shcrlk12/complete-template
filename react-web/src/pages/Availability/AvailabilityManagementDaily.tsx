import React, { useEffect } from "react";
import { AvailabilityTableContainer, MainSection } from "./Availability.styled";
import DailyTable from "@components/Table/DailyTable/DailyTable";
import { useDispatch } from "react-redux";

const AvailabilityManagementDaily = () => {
  const dispatch = useDispatch();

  useEffect(() => {}, []);

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
