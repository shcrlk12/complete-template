import * as React from "react";
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableFooter from "@mui/material/TableFooter";
import TablePagination from "@mui/material/TablePagination";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import IconButton from "@mui/material/IconButton";
import FirstPageIcon from "@mui/icons-material/FirstPage";
import KeyboardArrowLeft from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRight from "@mui/icons-material/KeyboardArrowRight";
import LastPageIcon from "@mui/icons-material/LastPage";
import Button from "../../Button/Button";
import styled from "styled-components";
import TableHead from "@mui/material/TableHead";
import { useNavigate } from "react-router";
import { TablePaginationActionsProps } from "@mui/material/TablePagination/TablePaginationActions";

const rows = [
  createData({
    id: "lillywilliams@gmail.com",
    name: "Lilly Williams",
    role: "Admin",
    lastLogin: "14 days ago",
  }),
  createData({
    id: "kjwon2@gmail.com",
    name: "Lilly Williams",
    role: "Admin",
    lastLogin: "14 days ago",
  }),
  createData({
    id: "kjwon3@gmail.com",
    name: "Lilly Williams",
    role: "Admin",
    lastLogin: "14 days ago",
  }),
];

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
    onPageChange(event, Number(event.currentTarget.innerHTML) - 1);
  };
  return (
    <Box sx={{ flex: "1 0 560px", ml: "40px" }}>
      <IconButton onClick={handleFirstPageButtonClick} disabled={page === 0} aria-label="first page">
        {theme.direction === "rtl" ? <LastPageIcon /> : <FirstPageIcon />}
      </IconButton>
      <IconButton onClick={handleBackButtonClick} disabled={page === 0} aria-label="previous page">
        {theme.direction === "rtl" ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
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
        {theme.direction === "rtl" ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
      </IconButton>
      <IconButton
        onClick={handleLastPageButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="last page">
        {theme.direction === "rtl" ? <FirstPageIcon /> : <LastPageIcon />}
      </IconButton>
    </Box>
  );
}

type UserTableData = {
  id: string;
  name: string;
  role: string;
  lastLogin: string;
};

function createData(userTableData: UserTableData) {
  return { ...userTableData };
}

const StyledTableCell = styled(TableCell)({
  [`&.${tableCellClasses.head}`]: {
    fontWeight: "bold",
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
});

export default function UserManagementTable() {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);
  const nav = useNavigate();

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows = page > 0 ? Math.max(0, (1 + page) * rowsPerPage - rows.length) : 0;

  const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>): void => {
    setRowsPerPage(parseInt(event.target?.value, 10));
    setPage(0);
  };

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 500 }} aria-label="custom pagination table">
        <TableHead>
          <TableRow>
            <StyledTableCell align="center">Email</StyledTableCell>
            <StyledTableCell align="center">Name</StyledTableCell>
            <StyledTableCell align="center">Role</StyledTableCell>
            <StyledTableCell align="center">Last Login</StyledTableCell>
            <StyledTableCell align="center">Delete</StyledTableCell>
            <StyledTableCell align="center">Edit</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {(rowsPerPage > 0 ? rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage) : rows).map((row) => (
            <TableRow key={row.id}>
              <StyledTableCell style={{ width: 250 }} align="center" component="th" scope="row">
                {row.id}
              </StyledTableCell>
              <StyledTableCell style={{ width: 150 }} align="center">
                {row.name}
              </StyledTableCell>
              <StyledTableCell style={{ width: 100 }} align="center">
                {row.role}
              </StyledTableCell>
              <StyledTableCell style={{ width: 100 }} align="center">
                {row.lastLogin}
              </StyledTableCell>
              <StyledTableCell style={{ width: 122 }} align="center">
                <Button isDangerous={true} width="100%" text="Delete" />
              </StyledTableCell>
              <StyledTableCell style={{ width: 122 }} align="center">
                <Button
                  isPrimary={false}
                  text="Edit"
                  width="100%"
                  onClick={() => {
                    nav("/users/modify");
                  }}
                />
              </StyledTableCell>
            </TableRow>
          ))}
          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <StyledTableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25, { label: "All", value: -1 }]}
              colSpan={6}
              count={rows.length}
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
}
