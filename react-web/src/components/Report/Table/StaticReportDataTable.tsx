import {
  Box,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableFooter,
  TableHead,
  TablePagination,
  TableRow,
  styled,
  tableCellClasses,
} from "@mui/material";
import React from "react";
import { JSX } from "react/jsx-runtime";
import FirstPageIcon from "@mui/icons-material/FirstPage";
import { KeyboardArrowLeft, KeyboardArrowRight, Padding } from "@mui/icons-material";
import LastPageIcon from "@mui/icons-material/LastPage";
import { useTheme } from "styled-components";
import { TablePaginationActionsProps } from "@mui/material/TablePagination/TablePaginationActions";

type TableHeader = {
  name: string;
  unit: string | null;
};

type TableDataItem = {
  value: string[];
};

type TableDataRow = {
  row: TableDataItem[];
};

export type StaticReportDataTableProps = {
  tableHeader: TableHeader[];
  tableData: TableDataRow;
};

const StyledTableCell = styled(TableCell)({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: "#C9DEE8",
    fontWeight: "bold",
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
});

function TablePaginationActions(props: TablePaginationActionsProps) {
  const theme = useTheme();
  const { count, page, rowsPerPage, onPageChange } = props;

  const handleFirstPageButtonClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    onPageChange(event, 0);
  };

  const handleBackButtonClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    onPageChange(event, page - 1);
  };

  const handleNextButtonClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    onPageChange(event, page + 1);
  };

  const handleLastPageButtonClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };
  const handlePageButtonClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    onPageChange(event, Number(event.currentTarget.innerHTML.split("<")[0]) - 1);
  };

  return (
    <Box style={{ width: "100%" }}>
      <IconButton onClick={handleFirstPageButtonClick} disabled={page === 0} aria-label="first page">
        <FirstPageIcon />
      </IconButton>
      <IconButton onClick={handleBackButtonClick} disabled={page === 0} aria-label="previous page">
        <KeyboardArrowLeft />
      </IconButton>
      {(() => {
        let newArr = [];
        for (
          var i = Math.floor(page / 5) * 5;
          i < Math.ceil(count / rowsPerPage) && i < Math.floor(page / 5) * 5 + 5;
          i++
        ) {
          newArr.push(
            <IconButton
              key={i}
              disabled={page === i}
              onClick={handlePageButtonClick}
              style={{
                fontSize: "15px",
                fontWeight: "bold",
              }}>
              {i + 1}
            </IconButton>,
          );
        }

        return newArr;
      })()}
      <IconButton
        onClick={handleNextButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="next page">
        <KeyboardArrowRight />
      </IconButton>
      <IconButton
        onClick={handleLastPageButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="last page">
        <LastPageIcon />
      </IconButton>
    </Box>
  );
}

const StaticReportDataTable = ({ tableHeader, tableData }: StaticReportDataTableProps) => {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>): void => {
    setRowsPerPage(parseInt(event.target?.value, 10));
    setPage(0);
  };

  const create = (): JSX.Element[] => {
    let newArr: JSX.Element[] = [];
    tableHeader.forEach((value) => {
      newArr.push(
        <StyledTableCell style={{ padding: "5px", border: "1px solid #427D9E" }} align="center">
          <div>{value.name}</div>
          <div>{value.unit && `[${value.unit}]`}</div>
        </StyledTableCell>,
      );
    });
    return newArr;
  };

  const test = () => {
    let newArr: JSX.Element[] = [];
    let temp = tableData.row.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    temp.map(({ value }, index) => {
      newArr.push(
        <TableRow key={index}>
          {value.map((item) => (
            <StyledTableCell style={{ padding: "5px", border: "1px solid #427D9E" }} align="center">
              {item}
            </StyledTableCell>
          ))}
        </TableRow>,
      );
    });

    return newArr;
  };
  return (
    <TableContainer style={{ padding: "10px" }} component={Paper}>
      <Table sx={{ minWidth: 500 }} aria-label="custom pagination table">
        <TableHead>
          <TableRow>{create()}</TableRow>
        </TableHead>
        <TableBody>{test()}</TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[10, 25, 50]}
              colSpan={6}
              count={tableData.row.length}
              rowsPerPage={rowsPerPage}
              page={page}
              labelRowsPerPage="Table Rows"
              labelDisplayedRows={({ from, to, count }) => `${from} - ${to} / ${count}`}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
              ActionsComponent={TablePaginationActions}
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
};

export default StaticReportDataTable;
