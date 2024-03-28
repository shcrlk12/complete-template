import React, { ReactNode } from "react";
import { tableClassesName } from "./Table";

type TableBodyProps = {
  className?: string;
  children: ReactNode;
};

function TableBody({ className, children }: TableBodyProps) {
  return (
    <div
      className={
        className === undefined ? tableClassesName.body : `${className}  ${tableClassesName.body}`
      }>
      {children}
    </div>
  );
}

export default TableBody;
