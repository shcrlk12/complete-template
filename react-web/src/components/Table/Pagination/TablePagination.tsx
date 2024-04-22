import ArrowButton from "../../Button/ArrowButton";
import { flexCenter, flexSpaceBetween } from "@components/style/Common";
import styled from "styled-components";

const TablePaginationContainer = styled.div`
  padding-top: 20px;
  height: 50px;
  ${flexCenter};
`;
const PaginationInner = styled.div`
  width: 100px;
  height: 100%;
  ${flexSpaceBetween};
`;

type TablePaginationProps = {
  leftButtonClick: any;
  rightButtonClick: any;
};

const TablePagination = ({ leftButtonClick, rightButtonClick }: TablePaginationProps) => {
  return (
    <TablePaginationContainer>
      <PaginationInner>
        <ArrowButton isLeft={true} onClick={leftButtonClick} />
        <ArrowButton isLeft={false} onClick={rightButtonClick} />
      </PaginationInner>
    </TablePaginationContainer>
  );
};

export default TablePagination;
