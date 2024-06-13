import { flexCenter } from "@components/style/Common";
import styled from "styled-components";

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
