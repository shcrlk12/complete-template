import React, { ReactNode } from "react";
import TablePagination from "./Pagination/TablePagination";

type TableFooterProps = {
  className?: string;
  children: ReactNode;
};
const TableFooter = ({ className, children }: TableFooterProps) => {
  return <div className={className}>{children}</div>;
};

export default TableFooter;
