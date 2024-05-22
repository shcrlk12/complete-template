import { flexCoulmnCenter } from "@components/style/Common";
import React, { ReactNode } from "react";
import styled from "styled-components";
import { tableClassesName } from "./Table";
import { yymmddToDate } from "@src/util/date";

const StyledTableCell = styled.div`
  ${flexCoulmnCenter};

  min-width: 62px;
  &:first-child {
    font-size: ${({ theme }) => theme.fontSize.small};
    min-width: 80px;
    position: sticky;
    left: 0;
    cursor: pointer;

    &:hover {
      background-color: ${({ theme }) => theme.colors.quaternary};
      font-weight: bold;
    }
  }
`;

type TableCellProps = {
  id?: string;
  className?: string;
  children: ReactNode;
  onClick?: any;
};

const TableCell = ({ id, className, children, onClick }: TableCellProps) => {
  return (
    <StyledTableCell
      onClick={onClick}
      id={id}
      className={className === undefined ? tableClassesName.cell : `${className}  ${tableClassesName.cell}`}>
      {children}
    </StyledTableCell>
  );
};

export default TableCell;
