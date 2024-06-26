import { flexCenter } from "@components/style/Common";
import styled from "styled-components";
import DeviceType from "./../../components/Report/DeviceType";

export const ReportContainer = styled.div`
  ${flexCenter};
`;

export const ReportInner = styled.div`
  width: 400px;
`;

export const Header = styled.div`
  font-size: ${({ theme }) => theme.fontSize.title};
  font-weight: bold;
  padding: 10px 10px;
  border-bottom: ${({ theme }) => theme.colors.quaternary} solid 1px;
`;

export const TopOnTableButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;

  margin-bottom: 10px;
`;

export const RightHeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const DateContainer = styled.div`
  margin-right: 30px;
  font-weight: bold;
`;

export const ReportTableContainer = styled.div`
  padding: 10px;
  width: 100%;
`;
