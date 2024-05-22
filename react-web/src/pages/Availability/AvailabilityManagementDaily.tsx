import { useEffect, useState } from "react";
import { AvailabilityTableContainer, MainSection } from "./Availability.styled";
import DailyTable from "@components/Table/DailyTable/DailyTable";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { useParams } from "react-router";
import { Dayjs } from "dayjs";

type AvailabilityStatus = {
  name: string;
  color: string;
};

export type MemoType = {
  engineerName: string;
  workTime: Dayjs | null;
  material: string;
  quantity: string;
  workType: string;
  inspection: string;
  etc: string;
};

export type Availability = {
  name: string;
  time: number;
};

type DetailData = {
  availability: Availability[];
  memo: MemoType;
  time: string;
  changed: boolean;
};

type DailyTurbineData = {
  turbineId: number;
  availability: number;
  data: DetailData[];
};

export type DailyTableData = {
  availability: number;
  date: string;
  turbinesNumber: number;
  turbines: DailyTurbineData[];
  statusList: AvailabilityStatus[];
};

const AvailabilityManagementDaily = () => {
  const { dispatch, navigate } = useInits();
  const [dailyTableData, setDailyTableData] = useState<DailyTableData>();
  const { year, month, day } = useParams();
  console.log(dailyTableData);

  useEffect(() => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch(`http://www.localhost:6789/api/wind-farm/daily/${year}/${month}/${day}`, {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      await statusOk(response);
      const data = await response.json();
      const json: DailyTableData = data.data;

      console.log(json);

      setDailyTableData(json);
    });
  }, [year, month, day]);

  return (
    <>
      <MainSection>
        <AvailabilityTableContainer>
          {dailyTableData ? <DailyTable dailyTableData={dailyTableData} /> : null}
        </AvailabilityTableContainer>
      </MainSection>
    </>
  );
};

export default AvailabilityManagementDaily;
