export const dateToyymmdd = (date: Date): string => {
  let { year, month, day } = parseyymmdd(date);

  return [year, ".", (month > 9 ? "" : "0") + month, ".", (day > 9 ? "" : "0") + day].join("");
};

export const yymmddToDate = (date: string): Date => {
  let splits = date.split(".");

  return new Date(
    Number.parseInt(splits[0]) + 2000,
    Number.parseInt(splits[1]) - 1,
    Number.parseInt(splits[2]),
    0,
    0,
    0,
    0,
  );
};

export const parseyymmdd = (date: Date) => {
  let year = date.getFullYear() % 100;
  let month = date.getMonth() + 1;
  let day = date.getDate();

  return { year, month, day };
};

export const parseyyyymmdd = (date: Date) => {
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let day = date.getDate();

  return { year, month, day };
};

export function isIsoDateString(str: string) {
  const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?$/;
  return iso8601Regex.test(str);
}
