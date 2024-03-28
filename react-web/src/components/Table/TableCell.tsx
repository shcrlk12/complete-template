import { flexCoulmnCenter } from "@components/style/Common";
import React, { ReactNode } from "react";
import styled from "styled-components";
import { tableClassesName } from "./Table";

const StyledTableCell = styled.div`
  ${flexCoulmnCenter};

  min-width: 62px;
  &:first-child {
    min-width: 80px;
    position: sticky;
    left: 0;
  }
`;

type TableCellProps = {
  className?: string;
  children: ReactNode;
};

const TableCell = ({ className, children }: TableCellProps) => {
  return (
    <StyledTableCell
      className={
        className === undefined ? tableClassesName.cell : `${className}  ${tableClassesName.cell}`
      }>
      {children}
    </StyledTableCell>
  );
};

export default TableCell;
