import { useEffect, useState } from "react";
import { AvailabilityTableContainer, MainSection } from "./Availability.styled";
import DailyTable from "@components/Table/DailyTable/DailyTable";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";

const AvailabilityManagementDaily = () => {
  const { dispatch, navigate } = useInits();
  const [turbineNumber, setTurbineNumber] = useState<number>(0);

  useEffect(() => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch("http://www.localhost:6789/api/wind-farm/daily/general-info", {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      await statusOk(response);
      const data = await response.json();
      const json = data.data;

      setTurbineNumber(json.turbineNumber);
    });
  }, []);

  return (
    <>
      <MainSection>
        <AvailabilityTableContainer>
          <DailyTable turbineNumber={turbineNumber} />
        </AvailabilityTableContainer>
      </MainSection>
    </>
  );
};

export default AvailabilityManagementDaily;
