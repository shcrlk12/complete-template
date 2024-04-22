import AvailStatusItem from "@components/Table/DailyTable/AvailStatusItem";
import TableCell from "../TableCell";
import Table from "../Table";
import TableHeader from "../TableHeader";
import TableRow from "../TableRow";
import TableBody from "../TableBody";
import { Status } from "../Table.styled";
import TablePagination from "./../Pagination/TablePagination";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import dayjs, { Dayjs } from "dayjs";

import {
  AvailStatusListContainer,
  ButtonContainer,
  MemoItemsContainer,
  TableContentInner,
  TableLeftInfo,
  TableRightInfo,
  WindFarmAvailContainer,
  StyledButton,
  StyledBodyRow,
} from "./DailyTable.styled";
import MemoInputItem from "./MemoInputItem";
import { Availability, DailyTableData, MemoType } from "@pages/Availability/AvailabilityManagementDaily";
import { MouseEvent, useEffect, useState } from "react";
import { useParams } from "react-router";
import useInits from "@src/hooks/useInits";
import { Paths } from "@src/Config";
import { JSX } from "react/jsx-runtime";
import styled from "styled-components";

type ColorType = {
  name: string;
  color: string;
};

type ColorType2 = {
  name: string;
  time: string;
};

type MapTypes = {
  time: number;
  color: string;
};

const DailyStatus = styled(Status)`
  cursor: pointer;
  &:hover {
    opacity: 0.7;
  }
`;

const initMemo = (): MemoType => {
  return {
    engineerName: "",
    workTime: null,
    material: "",
    quantity: "",
    workType: "",
    inspection: "",
    etc: "",
  };
};

const StyledTimePicker = styled(TimePicker)`
  height: 40px;

  & .MuiInputBase-input {
    padding: 5px;
  }
`;

type SelectedCellType = {
  row: number;
  column: number;
};

const DailyTable = ({ dailyTableData }: { dailyTableData: DailyTableData }) => {
  const { year, month, day } = useParams();
  const { dispatch, navigate } = useInits();
  const [map, setMap] = useState<Map<string, MapTypes>>(new Map());
  const [memo, setMemo] = useState<MemoType>(initMemo());
  const [seleectedCell, setSeleectedCell] = useState<any[]>([]);

  useEffect(() => {
    console.log(getStatusColor(dailyTableData.turbines[0].data[0].availability));

    let map1 = new Map<string, MapTypes>();
    map1.set("normal status", { time: 0, color: "#339D33" });
    map1.set("forced outage", { time: 0, color: "#D93333" });
    map1.set("scheduled maintenance", { time: 0, color: "#D9D633" });
    map1.set("information unavailable", { time: 0, color: "#C4D8F0" });
    map1.set("requested shutdown", { time: 0, color: "#33A1DE" });
    map1.set("etc", { time: 0, color: "#C4D8F0" });
    map1.set("environmental stop", { time: 0, color: "#D97733" });

    setMap(map1);
  }, []);

  const getItems = (map: Map<string, MapTypes>) => {
    const newArr: JSX.Element[] = [];

    map.forEach((value, key) => {
      newArr.push(<AvailStatusItem status={key} color={value.color} time={value.time} />);
    });

    return newArr;
  };

  const getStatusColor = (availability: Availability[]): string => {
    let statusTime = 0;
    let statusColor = "";

    availability.forEach((element) => {
      if (Number(element.time) > statusTime) {
        statusColor = map.get(element.name)?.color as string;
        statusTime = element.time;
      }
    });
    return statusColor;
  };

  const addHeaderList = () => {
    const newArr = [];
    for (let i = 0; i < dailyTableData.turbinesNumber; i++) {
      newArr.push(
        <TableCell key={i}>
          <div>WTG{String(i + 1).padStart(2, "0")}</div>
          <div>({dailyTableData.turbines[i].availability.toFixed(1)} %)</div>
        </TableCell>,
      );
    }
    return newArr;
  };
  const addStatus = (rowIndex: number) => {
    const newArr = [];
    for (let i = 0; i < dailyTableData.turbinesNumber; i++) {
      newArr.push(
        <TableCell key={i}>
          <DailyStatus
            id={`cell_${i}_${rowIndex}`}
            color={getStatusColor(dailyTableData.turbines[i].data[rowIndex].availability)}></DailyStatus>
        </TableCell>,
      );
    }
    return newArr;
  };
  const addBody = () => {
    const newArr = [];
    for (let i = 0; i < 24; i++) {
      newArr.push(
        <StyledBodyRow key={i}>
          <TableCell>{i % 2 === 0 && i !== 0 ? `${i}:00` : null}</TableCell>
          {addStatus(i)}
        </StyledBodyRow>,
      );
    }
    return newArr;
  };

  // Click Cell
  const clickTableStatus = (event: MouseEvent<HTMLElement>) => {
    let cellId = (event.target as any).id;
    let split = cellId.split("_");

    let turbineId = split[1];
    let row = split[2];

    if (turbineId !== undefined && row !== undefined) {
      let newMap = new Map<string, MapTypes>();

      let avail = dailyTableData.turbines[turbineId].data[row].availability;

      avail.forEach((item) => {
        let value = map.get(item.name);

        if (value) {
          value.time = Number((item.time / 60).toFixed(0));

          newMap.set(item.name, value);
        }
      });

      setMap(newMap);

      //set Memo
      let memo = dailyTableData.turbines[turbineId].data[row].memo;

      if (memo) {
        console.log(memo);

        setMemo({
          engineerName: memo.engineerName,
          workTime: dayjs(memo.workTime),
          material: memo.material,
          quantity: memo.quantity,
          workType: memo.workType,
          inspection: memo.inspection,
          etc: memo.etc,
        });
      } else {
        setMemo(initMemo());
      }

      //Set selected cell
      (event.target as any).className += " selected-cell";

      seleectedCell.forEach((target) => {
        target.className = (target.className as string).replace("selected-cell", " ");
      });

      let newArr = [];
      newArr.push(event.target);
      setSeleectedCell(newArr);
    }
  };

  return (
    <>
      <Table>
        <TableContentInner>
          <TableLeftInfo onClick={clickTableStatus}>
            <TableHeader>
              <TableRow>
                <TableCell>
                  <div>{year}</div>
                  <div>{`${month}.${day}`}</div>
                </TableCell>
                {addHeaderList()}
              </TableRow>
            </TableHeader>
            <TableBody>{addBody()}</TableBody>
          </TableLeftInfo>
          <TableRightInfo>
            <WindFarmAvailContainer>
              <span>단지 가동률 </span>
              <strong>{dailyTableData.availability.toFixed(1)}</strong>
              <span> %</span>
            </WindFarmAvailContainer>
            <MemoItemsContainer>
              <MemoInputItem
                id="memo-input-name"
                title="작업자"
                isInput={true}
                height="25px"
                text={memo.engineerName}
              />
              <div>시간</div>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DemoContainer components={["TimePicker"]}>
                  <StyledTimePicker
                    className="time-picker-container"
                    value={memo.workTime}
                    onChange={(newValue, context) => {
                      let newMemo = { ...memo };
                      newMemo.workTime = newValue;
                      setMemo(newMemo);
                    }}
                  />
                </DemoContainer>
              </LocalizationProvider>
              <MemoInputItem id="memo-input-material" title="자재" isInput={true} height="25px" text={memo.material} />
              <MemoInputItem id="memo-input-quantity" title="수량" isInput={true} height="25px" text={memo.quantity} />
              <MemoInputItem id="memo-input-type" title="작업 유형" isTextarea={true} text={memo.workType} />
              <MemoInputItem id="memo-input-history" title="점검 내역" isTextarea={true} text={memo.inspection} />
              <MemoInputItem id="memo-input-etc" title="기타(비고)" isTextarea={true} text={memo.etc} />
            </MemoItemsContainer>
            <AvailStatusListContainer>{getItems(map)}</AvailStatusListContainer>
            <ButtonContainer>
              <StyledButton text="저장" />
              <StyledButton text="취소" />
            </ButtonContainer>
          </TableRightInfo>
        </TableContentInner>
      </Table>
      <TablePagination
        leftButtonClick={() => {
          let date = new Date(dailyTableData.date);
          date.setDate(date.getDate() - 1);

          navigate(Paths.availability.daily.path + `/${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`);
        }}
        rightButtonClick={() => {
          let date = new Date(dailyTableData.date);
          date.setDate(date.getDate() + 1);

          navigate(Paths.availability.daily.path + `/${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`);
        }}
      />
    </>
  );
};

export default DailyTable;
