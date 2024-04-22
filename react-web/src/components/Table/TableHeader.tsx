import React, { ReactNode } from "react";
import styled from "styled-components";
import { tableClassesName } from "./Table";

export const StyledTableHeader = styled.div`
  height: 48px;
  position: sticky;
  top: 0;
  z-index: 20;
  background-color: ${({ theme }) => theme.colors.quaternary};
`;

type TableHeaderProps = {
  className?: string;
  children: ReactNode;
};

function TableHeader({ className, children }: TableHeaderProps) {
  return (
    <StyledTableHeader
      className={className === undefined ? tableClassesName.header : `${className}  ${tableClassesName.header}`}>
      {children}
    </StyledTableHeader>
  );
}

export default TableHeader;
