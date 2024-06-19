import { ReportTableProps } from "@components/Report/Table/ReportTable";
import { DailyReportDataTableProps, DailyTableRow } from "./DailyReport";
import { MemoReportDataTableProps, MemoTableRow } from "./MemoReport";

export const convertJsonToTableProps = (json: DailyReportDataTableProps | MemoReportDataTableProps) => {
  let tableProps: ReportTableProps = { tableHeader: [], tableData: { row: [] } };

  json.headerList.forEach((value) => tableProps.tableHeader.push({ name: value, unit: null }));

  json.tableData.forEach((data) => {
    let value: string[] = [];
    let key: keyof (DailyTableRow | MemoTableRow);

    for (key in data) {
      value.push(data[key]);
    }
    tableProps.tableData.row.push({ value: value });
  });

  return tableProps;
};

export const createDateOpsionSinceStartDate = (startDate: Date, date: Date, type: string): JSX.Element[] => {
  const now = new Date(Date.now());
  const options = [];

  if (type === "year") {
    for (let time = startDate.getFullYear(); time <= now.getFullYear(); time++)
      options.push(<option value={time}>{time}</option>);
  } else if (type === "month") {
    for (let time = 1; time <= 12; time++) options.push(<option value={time}>{time}</option>);
  } else if (type === "date") {
    const lastDate = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
    for (let time = 1; time <= lastDate; time++) options.push(<option value={time}>{time}</option>);
  }

  return options;
};
