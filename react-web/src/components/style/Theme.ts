import { DefaultTheme } from "styled-components";

const colors = {
  primary: "#309ED9",
  secondary: "#427D9E",
  tertiary: "#C4D8F0",
  quaternary: "#C9DEE8",
  black: "#0D171C",
  textOnPrimary: "#F2F5FA",
  white: "#F2F5FA",
  bg: "#F2F5FA",
  dangerous: "#E55656",
  activeDangerous: "#D93333",
};

const fontSize = {
  title: "22px",
  subTitle: "18px",
  text: "16px",
  small: "14px",
};

const size = {
  headerHeight: 60,
  headerMarginBottom: 2,
};

export type ColorsTypes = typeof colors;
export type FontSizeTypes = typeof fontSize;
export type SizeTypes = typeof size;

const theme: DefaultTheme = {
  colors,
  fontSize,
  size,
};

export default theme;
