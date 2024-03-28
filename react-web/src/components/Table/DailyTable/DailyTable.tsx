import AvailStatusItem from "@components/Table/DailyTable/AvailStatusItem";
import TableCell from "../TableCell";
import Table from "../Table";
import TableHeader from "../TableHeader";
import TableRow from "../TableRow";
import TableBody from "../TableBody";
import { Status } from "../Table.styled";
import TablePagination from "./../Pagination/TablePagination";

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

const DailyTable = () => {
  const addHeaderList = () => {
    const newArr = [];
    for (let i = 0; i < 30; i++) {
      newArr.push(
        <TableCell key={i}>
          <div>WTGXX</div>
          <div>(XX.x%)</div>
        </TableCell>,
      );
    }
    return newArr;
  };
  const addStatus = () => {
    const newArr = [];
    for (let i = 0; i < 30; i++) {
      newArr.push(
        <TableCell key={i}>
          <Status></Status>
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
          {addStatus()}
        </StyledBodyRow>,
      );
    }
    return newArr;
  };

  return (
    <>
      <Table>
        <TableContentInner>
          <TableLeftInfo>
            <TableHeader>
              <TableRow>
                <TableCell>
                  <div>2025</div>
                  <div>02.18</div>
                </TableCell>
                {addHeaderList()}
              </TableRow>
            </TableHeader>
            <TableBody>{addBody()}</TableBody>
          </TableLeftInfo>
          <TableRightInfo>
            <WindFarmAvailContainer>
              <span>단지 가동률 </span>
              <strong>99.8</strong>
              <span> %</span>
            </WindFarmAvailContainer>
            <MemoItemsContainer>
              <MemoInputItem id="memo-input-name" title="작업자" isInput={true} height="25px" text="홍길동" />
              <MemoInputItem id="memo-input-time" title="작업 시간" isInput={true} height="25px" text="11:30" />
              <MemoInputItem id="memo-input-material" title="자재" isInput={true} height="25px" text="모터" />
              <MemoInputItem id="memo-input-quantity" title="수량" isInput={true} height="25px" text="2개" />
              <MemoInputItem id="memo-input-type" title="작업 유형" isTextarea={true} text="어쩌고 저쩌고" />
              <MemoInputItem id="memo-input-history" title="점검 내역" isTextarea={true} text="어쩌고 저쩌고" />
              <MemoInputItem id="memo-input-etc" title="기타(비고)" isTextarea={true} text="어쩌고 저쩌고" />
            </MemoItemsContainer>
            <AvailStatusListContainer>
              <AvailStatusItem status="고장 정지 (Forced outage)" color="#D93333" time={10} />
              <AvailStatusItem status="정기점검 (Scheduled Maintenance)" color="#D9D633" time={10} />
              <AvailStatusItem status="요청 정지 (Requested Shutdown)" color="#33A1DE" time={10} />
              <AvailStatusItem status="환경 정지 (Environmental Stop)" color="#D97733" time={10} />
              <AvailStatusItem status="정상 (Normal Status)" color="#339D33" time={10} />
              <AvailStatusItem status="알수 없음 (Information Unavailable)" color="#7F8C8D" time={10} />
              <AvailStatusItem status="기타" color="#C4D8F0" time={10} />
            </AvailStatusListContainer>
            <ButtonContainer>
              <StyledButton text="저장" />
              <StyledButton text="취소" />
            </ButtonContainer>
          </TableRightInfo>
        </TableContentInner>
      </Table>
      <TablePagination />
    </>
  );
};

export default DailyTable;
