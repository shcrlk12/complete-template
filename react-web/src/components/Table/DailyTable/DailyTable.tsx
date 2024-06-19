import AvailStatusItem, {
  MINUS_BUTTON,
  PLUS_BUTTON,
  STATUS_CLASS_NAME,
} from "@components/Table/DailyTable/AvailStatusItem";
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
import dayjs from "dayjs";

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
import { ChangeEvent, MouseEvent, useEffect, useState } from "react";
import { useParams } from "react-router";
import useInits from "@src/hooks/useInits";
import { Paths, backendServerIp } from "@src/Config";
import { JSX } from "react/jsx-runtime";
import styled from "styled-components";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";
import { flexCenter, flexRowReverse } from "@components/style/Common";
import theme from "@components/style/Theme";
import { fetchData, statusOk } from "@src/util/fetch";
import { ROLE_USER } from "@reducers/userActions";

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
  event: any;
  row: number;
  column: number;
};

const TotalTimeContainer = styled.div`
  ${flexRowReverse}
  padding: 5px 20px;

  & div:first-child {
    margin-right: 10px;
    width: 40px;
    height: 100%;
    font-size: ${({ theme }) => theme.fontSize.subTitle};
    color: ${({ theme }) => theme.colors.black};
    ${flexCenter}
  }
  & div:last-child {
  }
`;

const DailyTable = ({ dailyTableData }: { dailyTableData: DailyTableData }) => {
  const { year, month, day } = useParams();
  const { dispatch, navigate } = useInits();
  const [map, setMap] = useState<Map<string, MapTypes>>(new Map());
  const [memo, setMemo] = useState<MemoType>(initMemo());
  const [seleectedCell, setSeleectedCell] = useState<SelectedCellType[]>([]);
  const [tableData, setTableData] = useState(dailyTableData);

  const { startOfWarrantyDate } = useSelector((store: RootState) => store.appReducer);
  const userRole = useSelector((store: RootState) => store.userReducer.user.role);
  const startWarrantyDate = new Date(startOfWarrantyDate);

  useEffect(() => {
    initAvailabilityMap();
    setTableData(dailyTableData);
  }, [dailyTableData]);

  const getTotalTime = (map: Map<string, MapTypes>): number => {
    let result = 0;
    for (let value of map.values()) {
      result += value.time;
    }
    return result;
  };

  const getItems = (map: Map<string, MapTypes>) => {
    const newArr: JSX.Element[] = [];

    map.forEach((value, key) => {
      newArr.push(
        <AvailStatusItem
          status={key}
          color={value.color}
          time={value.time}
          onClick={(event: MouseEvent<HTMLDivElement>) => {
            let statusName = event.currentTarget.querySelector("." + STATUS_CLASS_NAME)?.innerHTML;

            const target = event.target as HTMLInputElement;
            let buttonType = target.innerHTML;

            if (statusName === undefined) return;
            let newMap = cloneMap(map);
            let mapTypes = newMap.get(statusName);

            if (mapTypes === undefined) return;

            if (buttonType === PLUS_BUTTON) {
              let totalTime = getTotalTime(map);
              mapTypes.time = totalTime + 5 > 60 ? 60 - totalTime + mapTypes.time : mapTypes.time + 5;
            } else if (buttonType === MINUS_BUTTON) {
              mapTypes.time = mapTypes.time - 5 < 0 ? 0 : mapTypes.time - 5;
            }

            setMap(newMap);
          }}
        />,
      );
    });

    return newArr;
  };

  const getStatusColor = (availability: Availability[]): string => {
    // availability 속성이 하나만 있으면 gradiention 을 사용 하지 못함.
    if (availability.length === 1) {
      return map.get(availability[0].name)?.color || "";
    }

    // availability 속성이 2개 이상일 때 실행
    let statusColor = `linear-gradient(to right `;
    let beforePositoin = 0;

    let totalTime = 0;
    for (let avail of availability) {
      totalTime += avail.time;
    }

    availability?.forEach((element, index) => {
      let currentPosition = Number(((element.time / totalTime) * 100).toFixed(1));

      if (!index) {
        statusColor += `, ${map.get(element.name)?.color} ${currentPosition}%`;
        beforePositoin = Number(((element.time / totalTime) * 100).toFixed(1));
      } else {
        statusColor += `, ${map.get(element.name)?.color} ${beforePositoin}%`;
        statusColor += `, ${map.get(element.name)?.color} ${beforePositoin + currentPosition}%`;
        beforePositoin = beforePositoin + currentPosition;
      }
    });

    statusColor += " );";
    return statusColor;
  };

  const addHeaderList = () => {
    const newArr = [];
    for (let i = 0; i < tableData.turbinesNumber; i++) {
      newArr.push(
        <TableCell key={i}>
          <div style={{ fontSize: "14px" }}>WTG{String(i + 1).padStart(2, "0")}</div>
          <div style={{ fontSize: "14px" }}>({tableData.turbines[i]?.availability.toFixed(1)} %)</div>
        </TableCell>,
      );
    }
    return newArr;
  };
  var rowIndexArray = new Array(tableData.turbinesNumber).fill(0);

  const addStatus = (rowIndex: number) => {
    const newArr = [];
    for (let i = 0; i < tableData.turbinesNumber; i++) {
      let turbineTime = new Date(tableData.turbines[i].data[rowIndexArray[i]].time);

      let baseTime = new Date(tableData.date); // 사용 안함
      let columnTime = new Date(baseTime);
      columnTime.setHours(baseTime.getHours() + rowIndex);

      let className = tableData?.turbines[i]?.data[rowIndexArray[i]]?.changed ? "is-memo" : "";

      if (turbineTime.getTime() === columnTime.getTime()) {
        newArr.push(
          <TableCell key={i}>
            <DailyStatus
              id={`cell_${i}_${rowIndex}`}
              color={getStatusColor(tableData?.turbines[i]?.data[rowIndexArray[i]]?.availability)}
              className={className}></DailyStatus>
          </TableCell>,
        );
        rowIndexArray[i]++;
      } else {
        newArr.push(
          <TableCell key={i}>
            <DailyStatus id={`cell_${i}_${rowIndex}`} color="#000" className={className}></DailyStatus>
          </TableCell>,
        );
      }
    }
    return newArr;
  };
  const addBody = () => {
    const newArr = [];
    let now = new Date(Date.now());
    let date = new Date(tableData.date);
    let index = 0;
    let endDate;

    if (date.setDate(date.getDate() + 1) < Date.now()) endDate = new Date(date.getTime());
    else {
      endDate = new Date(now);
      endDate.setHours(endDate.getHours() - 1);
    }

    let startDate = new Date(date.setDate(date.getDate() - 1));

    while (startDate < endDate) {
      newArr.push(
        <StyledBodyRow key={index}>
          <TableCell className="no-dragable" id={`cell_-1_${index}`}>{`${index}:00`}</TableCell>
          {addStatus(index)}
        </StyledBodyRow>,
      );

      index++;
      startDate.setHours(startDate.getHours() + 1);
    }
    return newArr;
  };

  const initAvailabilityMap = () => {
    let newMap = new Map<string, MapTypes>();

    tableData.statusList.forEach((item) => {
      newMap.set(item.name, { time: 0, color: item.color });
    });
    setMap(newMap);
  };

  const clearTimeMap = (map: any) => {
    let newMap = new Map<string, MapTypes>();

    for (var key of map.keys()) {
      let value: MapTypes = map.get(key) as MapTypes;

      newMap.set(key, {
        ...value,
        time: 0,
      });
    }

    return newMap;
  };

  const cloneMap = (map: any) => {
    let newMap = new Map<string, MapTypes>();

    for (var key of map.keys()) {
      let value: MapTypes = map.get(key) as MapTypes;

      newMap.set(key, {
        ...value,
      });
    }

    return newMap;
  };
  // Click Cell
  const clickTableStatus = (event: MouseEvent<HTMLElement>) => {
    let { turbineId, time } = parseRowColumn(event);

    let newArr: SelectedCellType[] = [...seleectedCell];

    // click cell
    if (turbineId !== undefined && turbineId != -1 && time !== undefined) {
      let newMap = clearTimeMap(map);

      let avail = tableData.turbines[turbineId].data[time].availability;

      avail.forEach((item) => {
        let value = map.get(item.name);

        if (value) {
          value.time = Number((item.time / 60).toFixed(0));

          newMap.set(item.name, value);
        }
      });

      setMap(newMap);

      //set Memo
      let memo = tableData.turbines[turbineId].data[time].memo;

      if (memo) {
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
      if (event.ctrlKey) {
        if (event !== null) {
          if (!(event.target as any).className.includes("selected-cell")) {
            (event.target as any).className += " selected-cell";
            newArr.push({ event: event.target, row: time, column: turbineId });
          }
        }
      } else if (event.shiftKey) {
        removeAllSelectedCell(seleectedCell);
        newArr = [];
        let lastRow = seleectedCell[0].row;
        let lastColumn = seleectedCell[0].column;

        if (Number(lastRow) <= Number(time)) {
          if (Number(lastColumn) <= Number(turbineId)) {
            for (let k = Number(lastColumn); k <= Number(turbineId); k++) {
              for (let j = Number(lastRow); j <= Number(time); j++) {
                let event2 = event.currentTarget.querySelector(`#cell_${k}_${j}`);

                if (event2 !== null) {
                  if (!event2.className.includes("selected-cell")) {
                    event2.className += " selected-cell";
                    newArr.push({ event: event2, row: j, column: k });
                  }
                }
              }
            }
          } else {
            for (let k = Number(lastColumn); k >= Number(turbineId); k--) {
              for (let j = Number(lastRow); j <= Number(time); j++) {
                let event2 = event.currentTarget.querySelector(`#cell_${k}_${j}`);

                if (event2 !== null) {
                  if (!event2.className.includes("selected-cell")) {
                    event2.className += " selected-cell";
                    newArr.push({ event: event2, row: j, column: k });
                  }
                }
              }
            }
          }
        } else {
          if (Number(lastColumn) <= Number(turbineId)) {
            for (let k = Number(lastColumn); k <= Number(turbineId); k++) {
              for (let j = Number(lastRow); j >= Number(time); j--) {
                let event2 = event.currentTarget.querySelector(`#cell_${k}_${j}`);

                if (event2 !== null) {
                  if (!event2.className.includes("selected-cell")) {
                    event2.className += " selected-cell";
                    newArr.push({ event: event2, row: j, column: k });
                  }
                }
              }
            }
          } else {
            for (let k = Number(lastColumn); k >= Number(turbineId); k--) {
              for (let j = Number(lastRow); j >= Number(time); j--) {
                let event2 = event.currentTarget.querySelector(`#cell_${k}_${j}`);

                if (event2 !== null) {
                  if (!event2.className.includes("selected-cell")) {
                    event2.className += " selected-cell";
                    newArr.push({ event: event2, row: j, column: k });
                  }
                }
              }
            }
          }
        }
      } else {
        removeAllSelectedCell(seleectedCell);
        newArr = [];
        if (event !== null) {
          if (!(event.target as any).className.includes("selected-cell")) {
            (event.target as any).className += " selected-cell";
            newArr.push({ event: event.target, row: time, column: turbineId });
          }
        }
      }
    }
    // click row header
    else if (Number(turbineId) === -1 && time !== undefined) {
      // ctrl click logic
      if (event.ctrlKey) {
        for (let i = 0; i < tableData.turbinesNumber; i++) {
          let event2 = event.currentTarget.querySelector(`#cell_${i}_${time}`);

          if (event2 !== null) {
            if (!event2.className.includes("selected-cell")) {
              event2.className += " selected-cell";
              newArr.push({ event: event2, row: time, column: i });
            }
          }
        }
      }

      // shift click logic
      else if (event.shiftKey) {
        removeAllSelectedCell(seleectedCell);
        newArr = [];

        let lastRow = seleectedCell[0].row;

        if (Number(lastRow) <= Number(time)) {
          for (let j = Number(lastRow); j <= Number(time); j++) {
            for (let i = 0; i < Number(tableData.turbinesNumber); i++) {
              let event2 = event.currentTarget.querySelector(`#cell_${i}_${j}`);

              if (event2 !== null) {
                if (!event2.className.includes("selected-cell")) {
                  event2.className += " selected-cell";
                  newArr.push({ event: event2, row: j, column: i });
                }
              }
            }
          }
        } else if (Number(lastRow) > Number(time)) {
          for (let j = Number(lastRow); j >= Number(time); j--) {
            for (let i = 0; i < Number(tableData.turbinesNumber); i++) {
              let event2 = event.currentTarget.querySelector(`#cell_${i}_${j}`);

              if (event2 !== null) {
                if (!event2.className.includes("selected-cell")) {
                  event2.className += " selected-cell";
                  newArr.push({ event: event2, row: j, column: i });
                }
              }
            }
          }
        }
      }
      // normal click logic
      else {
        removeAllSelectedCell(seleectedCell);
        newArr = [];

        for (let i = 0; i < tableData.turbinesNumber; i++) {
          let event2 = event.currentTarget.querySelector(`#cell_${i}_${time}`);

          if (event2 !== null) {
            event2.className += " selected-cell";
          }

          newArr.push({ event: event2, row: time, column: i });
        }
      }
    }
    setSeleectedCell(newArr);
    console.log(newArr);
  };

  const removeAllSelectedCell = (cellList: SelectedCellType[]) => {
    cellList.forEach((cell) => {
      cell.event.className = (cell.event.className as string).replace("selected-cell", " ");
    });
  };

  const addSelectedCell = (event: MouseEvent<HTMLElement>) => {
    (event.target as any).className += " selected-cell";
  };

  const parseRowColumn = (event: MouseEvent<HTMLElement>) => {
    let cellId = (event.target as any).id;
    let split = cellId.split("_");

    return { turbineId: split[1] as number, time: split[2] as number };
  };

  const changeInput = (event: ChangeEvent<HTMLInputElement>, variableName: string) => {
    let value = event.target.value;
    const newMemo = {
      ...memo,
      [variableName]: value,
    };

    setMemo(newMemo);
  };

  const makeAvailabilityObject = (map: Map<string, MapTypes>) => {
    let availability: Availability[] = [];

    map.forEach((value, key) => {
      availability.push({ time: value.time * 60, name: key });
    });

    return availability;
  };

  const getTimestamps = (seleectedCell: SelectedCellType[]) => {
    let result: any[] = [];

    seleectedCell.forEach((value) => {
      let timestamp = new Date(tableData.date);

      timestamp.setHours(value.row); // GMT + 9 시간 적용

      result.push({
        turbineId: value.column,
        timestamp: timestamp,
      });
    });

    return result;
  };
  return (
    <>
      <Table>
        <TableContentInner>
          <TableLeftInfo onClick={clickTableStatus}>
            <TableHeader>
              <TableRow>
                <TableCell
                  onClick={() => {
                    let date = new Date(tableData.date);

                    navigate(Paths.availability.annually.path + `/${date.getFullYear()}`);
                  }}>
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
              <strong>{tableData.availability?.toFixed(1)}</strong>
              <span> %</span>
            </WindFarmAvailContainer>
            <MemoItemsContainer>
              <MemoInputItem
                id="memo-input-name"
                title="작업자"
                isInput={true}
                height="25px"
                text={memo.engineerName}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "engineerName")}
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
              <MemoInputItem
                id="memo-input-material"
                title="자재"
                isInput={true}
                height="25px"
                text={memo.material}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "material")}
              />
              <MemoInputItem
                id="memo-input-quantity"
                title="수량"
                isInput={true}
                height="25px"
                text={memo.quantity}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "quantity")}
              />
              <MemoInputItem
                id="memo-input-type"
                title="작업 유형"
                isTextarea={true}
                text={memo.workType}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "workType")}
              />
              <MemoInputItem
                id="memo-input-history"
                title="점검 내역"
                isTextarea={true}
                text={memo.inspection}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "inspection")}
              />
              <MemoInputItem
                id="memo-input-etc"
                title="기타(비고)"
                isTextarea={true}
                text={memo.etc}
                onChange={(event: ChangeEvent<HTMLInputElement>) => changeInput(event, "etc")}
              />
            </MemoItemsContainer>
            <AvailStatusListContainer>
              {getItems(map)}
              <TotalTimeContainer>
                <div>{getTotalTime(map)}</div>
                <div>합계 </div>
              </TotalTimeContainer>
            </AvailStatusListContainer>
            <ButtonContainer>
              <StyledButton
                text="저장"
                disabled={userRole === ROLE_USER}
                onClick={() => {
                  if (getTotalTime(map) < 60) {
                    alert("시간 합계가 60분 이하 입니다.");
                    return;
                  }

                  fetchData(dispatch, navigate, async () => {
                    const response = await fetch(`http://${backendServerIp}/api/wind-farm/daily/register`, {
                      mode: "cors",
                      method: "POST",
                      credentials: "include",
                      headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                      },
                      body: JSON.stringify({
                        timestamps: getTimestamps(seleectedCell),
                        memo: memo,
                        availability: makeAvailabilityObject(map),
                      }),
                    });

                    await statusOk(response);

                    const json = await response.json();
                    const data = json.data;
                  });

                  let newTableData = { ...tableData };

                  seleectedCell.forEach((data) => {
                    newTableData.turbines[data.column].data[data.row].changed = true;
                    newTableData.turbines[data.column].data[data.row].memo = memo;
                    newTableData.turbines[data.column].data[data.row].availability = makeAvailabilityObject(map);
                  });

                  setTableData(newTableData);
                }}
              />
              <StyledButton
                text="시간 초기화"
                disabled={userRole === ROLE_USER}
                onClick={() => {
                  initAvailabilityMap();
                }}
              />
            </ButtonContainer>
          </TableRightInfo>
        </TableContentInner>
      </Table>
      <TablePagination
        leftButtonClick={() => {
          let date = new Date(tableData.date);
          date.setDate(date.getDate() - 1);

          if (startWarrantyDate.getTime() <= date.getTime()) {
            removeAllSelectedCell(seleectedCell);
            navigate(Paths.availability.daily.path + `/${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`);
          }
        }}
        rightButtonClick={() => {
          let date = new Date(tableData.date);
          date.setDate(date.getDate() + 1);

          if (date.getTime() < new Date(Date.now()).getTime()) {
            removeAllSelectedCell(seleectedCell);
            navigate(Paths.availability.daily.path + `/${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`);
          }
        }}
      />
    </>
  );
};

export default DailyTable;
