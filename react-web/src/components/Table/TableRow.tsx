import React, { ReactNode } from "react";
import styled from "styled-components";
import { tableClassesName } from "./Table";

const StyledTableRow = styled.div`
  min-height: 15px;
  display: flex;
`;

type TableRowProps = {
  className?: string;
  children: ReactNode;
};

const TableRow = ({ className, children }: TableRowProps) => {
  return (
    <StyledTableRow
      className={
        className === undefined ? tableClassesName.row : `${className}  ${tableClassesName.row}`
      }>
      {children}
    </StyledTableRow>
  );
};

export default TableRow;
