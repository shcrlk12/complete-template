import Table from "../Table";
import TableHeader from "../TableHeader";
import TableCell from "../TableCell";
import TableBody from "../TableBody";
import TableRow from "../TableRow";
import TablePagination from "../Pagination/TablePagination";
import { Status } from "../Table.styled";

const AnnualTable = () => {
  const addHeaderList = () => {
    const newArr = [];
    for (let i = 0; i < 30; i++) {
      newArr.push(
        <TableCell key={i}>
          <div>WTGXX</div>
          <div>(XX.x%)</div>
        </TableCell>,
      );
    }
    return newArr;
  };

  const addStatus = () => {
    const newArr = [];
    for (let i = 0; i < 30; i++) {
      newArr.push(
        <TableCell key={i}>
          <Status></Status>
        </TableCell>,
      );
    }
    return newArr;
  };
  const addBody = () => {
    const newArr = [];
    for (let i = 0; i < 100; i++) {
      newArr.push(
        <TableRow key={i}>
          <TableCell>XX.02.01</TableCell>
          {addStatus()}
        </TableRow>,
      );
    }
    return newArr;
  };

  return (
    <>
      <Table>
        <TableHeader>
          <TableRow>
            <TableCell>
              <div>20XX </div>
              <div>[X년차]</div>
            </TableCell>
            {addHeaderList()}
          </TableRow>
        </TableHeader>
        <TableBody>{addBody()}</TableBody>
      </Table>
      <TablePagination />
    </>
  );
};

export default AnnualTable;
