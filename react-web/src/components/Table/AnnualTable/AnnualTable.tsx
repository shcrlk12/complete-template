import Table from "../Table";
import TableHeader from "../TableHeader";
import TableCell from "../TableCell";
import TableBody from "../TableBody";
import TableRow from "../TableRow";
import TablePagination from "../Pagination/TablePagination";
import { Status } from "../Table.styled";
import { AnnuallyTableData } from "@pages/Availability/AvailabilityManagementAnnually";
import { dateToyymmdd, parseyyyymmdd, yymmddToDate } from "@src/util/date";
import theme from "@components/style/Theme";
import { useNavigate } from "react-router";
import useInits from "@src/hooks/useInits";
import { Paths } from "@src/Config";

const AnnualTable = ({ annuallyTableData }: { annuallyTableData: AnnuallyTableData }) => {
  const { dispatch, navigate } = useInits();

  const addHeaderList = () => {
    const newArr = [];
    for (let i = 0; i < annuallyTableData.turbinesNumber; i++) {
      newArr.push(
        <TableCell key={i}>
          <div>WTG{String(i + 1).padStart(2, "0")}</div>
          <div>({annuallyTableData.turbines[i].availability.toFixed(1)} %)</div>
        </TableCell>,
      );
    }
    return newArr;
  };

  const getColorByAvailability = (availability: number): string => {
    if (availability < 30) {
      return theme.colors.red;
    }
    if (availability < 90) {
      return theme.colors.orange;
    }
    if (availability < 95) {
      return theme.colors.yellow;
    } else {
      return theme.colors.green;
    }
  };

  const addStatus = (rowIndex: number) => {
    const newArr = [];

    for (let i = 0; i < annuallyTableData.turbinesNumber; i++) {
      newArr.push(
        <TableCell key={i}>
          <Status color={getColorByAvailability(annuallyTableData.turbines[i].data[rowIndex].availability)}></Status>
        </TableCell>,
      );
    }
    return newArr;
  };

  let startDate = new Date(annuallyTableData?.startTimeOfYears);
  let aYearLaterDate = new Date(annuallyTableData?.startTimeOfYears);
  aYearLaterDate.setFullYear(aYearLaterDate.getFullYear() + 1);

  let now = new Date(Date.now());

  let endDate = now < aYearLaterDate ? now : aYearLaterDate;

  const onClickCell = (event: React.MouseEvent<HTMLDivElement>) => {
    const clickDate = yymmddToDate(event.currentTarget.textContent as string);

    const { year, month, day } = parseyyyymmdd(clickDate);

    navigate(`${Paths.availability.daily.path}/${year}/${month}/${day}`);
  };

  const addBody = () => {
    const newArr = [];
    let rowIndex = 0;

    while (startDate < endDate) {
      dateToyymmdd(startDate);

      let dateTime = dateToyymmdd(startDate);

      newArr.push(
        <TableRow key={dateTime}>
          <TableCell onClick={onClickCell}>{dateTime}</TableCell>
          {addStatus(rowIndex)}
        </TableRow>,
      );
      startDate.setDate(startDate.getDate() + 1);
      rowIndex++;
    }
    return newArr;
  };

  return (
    <>
      <Table>
        <TableHeader>
          <TableRow>
            <TableCell>
              <div>{startDate.getFullYear()}</div>
              <div>{annuallyTableData?.yearsOfWarranty}년차</div>
            </TableCell>
            {addHeaderList()}
          </TableRow>
        </TableHeader>
        <TableBody>{addBody()}</TableBody>
      </Table>
      <TablePagination
        leftButtonClick={() => {
          let date = new Date(annuallyTableData.date);
          date.setFullYear(date.getFullYear() - 1);

          navigate(Paths.availability.annually.path + `/${date.getFullYear()}`);
        }}
        rightButtonClick={() => {
          let date = new Date(annuallyTableData.date);
          date.setDate(date.getDate() + 1);

          let now = new Date(Date.now());

          if (date.getTime() < now.getTime()) {
            navigate(Paths.availability.daily.path + `/${date.getFullYear()}`);
          }
        }}
      />
    </>
  );
};

export default AnnualTable;
