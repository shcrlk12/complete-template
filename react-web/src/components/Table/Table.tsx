import React, { ReactNode } from "react";
import styled from "styled-components";

/**
 * table elements의 class name을 정의
 */
export const tableClassesName = {
  table: "availability-table",
  header: "availability-table-header",
  body: "availability-table-body",
  row: "availability-table-row",
  cell: "availability-table-cell",
};

const StyledTable = styled.div`
  border-radius: 12px;
  overflow: auto;
  border: 1px solid ${({ theme }) => theme.colors.tertiary};
  position: relative;

  & .${tableClassesName.header} .${tableClassesName.row} {
    height: 100%;
  }
  & .${tableClassesName.body} .${tableClassesName.row} {
    height: 15px;
  }
`;

type TableProps = {
  className?: string;
  children: ReactNode;
};

function Table({ className, children }: TableProps) {
  return (
    <StyledTable
      className={
        className === undefined ? tableClassesName.table : `${className}  ${tableClassesName.table}`
      }>
      {children}
    </StyledTable>
  );
}

export default Table;
